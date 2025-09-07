
package com.codeiguanas.promptgpt.web.controller;

import com.codeiguanas.promptgpt.domain.TemplateRenderer;
import com.codeiguanas.promptgpt.domain.lint.LintFinding;
import com.codeiguanas.promptgpt.domain.lint.LintService;
import com.codeiguanas.promptgpt.providers.GenerationResult;
import com.codeiguanas.promptgpt.providers.ProviderType;
import com.codeiguanas.promptgpt.web.config.ProviderRegistry;
import com.codeiguanas.promptgpt.web.dto.*;
import com.codeiguanas.promptgpt.web.repo.FileTemplateRepository;
import com.codeiguanas.promptgpt.web.service.CostGuard;
import com.codeiguanas.promptgpt.web.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenerationController {

    private final TemplateRenderer renderer = new TemplateRenderer();
    private final LintService lintService = new LintService();
    private final ProviderRegistry providers;
    private final FileTemplateRepository repo;
    private final CostGuard costGuard;
    private final RateLimiterService rateLimiter;

    public GenerationController(ProviderRegistry providers, FileTemplateRepository repo, CostGuard costGuard, RateLimiterService rateLimiter) {
        this.providers = providers;
        this.repo = repo;
        this.costGuard = costGuard;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/generate")
    public GenerateResponse generate(@Valid @RequestBody GenerateRequest req, HttpServletRequest request) {
        String rendered = renderer.render(req.getTemplateContent(), req.getVariables() == null ? Map.of() : req.getVariables());
        List<LintFinding> findings = lintService.lint(rendered);
        String key = clientKey(request);
        rateLimiter.consumeRequestOrThrow(key);
        int tokensInEstimate = com.codeiguanas.promptgpt.domain.TokenEstimator.estimate(req.getModel(), (req.getSystem()==null?"":req.getSystem())+"\n"+rendered);
        rateLimiter.consumeTokensOrThrow(key, (long) tokensInEstimate + Math.max(1, req.getExpectedOutputTokens()));
        costGuard.guard(req.getModel(), req.getSystem(), rendered, Math.max(1, req.getExpectedOutputTokens()));
        ProviderType type = parseType(req.getProvider());
        GenerationResult result = providers.get(type).invoke(req.getModel(), req.getSystem(), rendered, Map.of());
        return new GenerateResponse(rendered, result.text(), result.tokensIn(), result.tokensOut(), result.latencyMs(), findings);
    }

    @PostMapping("/generate-by-id/{id}")
    public GenerateResponse generateById(@PathVariable String id, @RequestBody GenerateByIdRequest req, HttpServletRequest request) {
        var t = repo.get(id).orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        String rendered = renderer.render(t.content(), req.getVariables() == null ? Map.of() : req.getVariables());
        List<LintFinding> findings = lintService.lint(rendered);
        String key = clientKey(request);
        rateLimiter.consumeRequestOrThrow(key);
        int tokensInEstimate = com.codeiguanas.promptgpt.domain.TokenEstimator.estimate(req.getModel(), (req.getSystem()==null?"":req.getSystem())+"\n"+rendered);
        rateLimiter.consumeTokensOrThrow(key, (long) tokensInEstimate + Math.max(1, req.getExpectedOutputTokens()));
        costGuard.guard(req.getModel(), req.getSystem(), rendered, Math.max(1, req.getExpectedOutputTokens()));
        ProviderType type = parseType(req.getProvider());
        GenerationResult result = providers.get(type).invoke(req.getModel(), req.getSystem(), rendered, Map.of());
        return new GenerateResponse(rendered, result.text(), result.tokensIn(), result.tokensOut(), result.latencyMs(), findings);
    }

    @PostMapping(value = "/generate/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateStream(@Valid @RequestBody GenerateRequest req, HttpServletRequest request) {
        String rendered = renderer.render(req.getTemplateContent(), req.getVariables() == null ? Map.of() : req.getVariables());
        List<LintFinding> findings = lintService.lint(rendered);
        String key = clientKey(request);
        rateLimiter.consumeRequestOrThrow(key);
        int tokensInEstimate = com.codeiguanas.promptgpt.domain.TokenEstimator.estimate(req.getModel(), (req.getSystem()==null?"":req.getSystem())+"\n"+rendered);
        rateLimiter.consumeTokensOrThrow(key, (long) tokensInEstimate + Math.max(1, req.getExpectedOutputTokens()));
        costGuard.guard(req.getModel(), req.getSystem(), rendered, Math.max(1, req.getExpectedOutputTokens()));
        var type = parseType(req.getProvider());

        SseEmitter emitter = new SseEmitter(0L);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                emitter.send(SseEmitter.event().name("lint").data(findings));
                providers.get(type).stream(req.getModel(), req.getSystem(), rendered, Map.of(),
                    delta -> { try { emitter.send(SseEmitter.event().name("delta").data(delta)); } catch (Exception ignored) {} },
                    usage -> { try { emitter.send(SseEmitter.event().name("usage").data(usage)); } catch (Exception ignored) {} }
                );
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally { es.shutdown(); }
        });
        return emitter;
    }

    @PostMapping(value = "/generate-by-id/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateByIdStream(@PathVariable String id, @RequestBody GenerateByIdRequest req, HttpServletRequest request) {
        var t = repo.get(id).orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        String rendered = renderer.render(t.content(), req.getVariables() == null ? Map.of() : req.getVariables());
        List<LintFinding> findings = lintService.lint(rendered);
        String key = clientKey(request);
        rateLimiter.consumeRequestOrThrow(key);
        int tokensInEstimate = com.codeiguanas.promptgpt.domain.TokenEstimator.estimate(req.getModel(), (req.getSystem()==null?"":req.getSystem())+"\n"+rendered);
        rateLimiter.consumeTokensOrThrow(key, (long) tokensInEstimate + Math.max(1, req.getExpectedOutputTokens()));
        costGuard.guard(req.getModel(), req.getSystem(), rendered, Math.max(1, req.getExpectedOutputTokens()));
        var type = parseType(req.getProvider());

        SseEmitter emitter = new SseEmitter(0L);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                emitter.send(SseEmitter.event().name("lint").data(findings));
                providers.get(type).stream(req.getModel(), req.getSystem(), rendered, Map.of(),
                    delta -> { try { emitter.send(SseEmitter.event().name("delta").data(delta)); } catch (Exception ignored) {} },
                    usage -> { try { emitter.send(SseEmitter.event().name("usage").data(usage)); } catch (Exception ignored) {} }
                );
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally { es.shutdown(); }
        });
        return emitter;
    }

    private ProviderType parseType(String s) {
        if (s == null) return ProviderType.ECHO;
        try { return ProviderType.valueOf(s.trim().toUpperCase()); } catch (Exception e) { return ProviderType.ECHO; }
    }
    private String clientKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-Api-Key"); if(apiKey!=null && !apiKey.isBlank()) return "key:"+apiKey;
        String ip = request.getHeader("X-Forwarded-For"); if(ip==null||ip.isBlank()) ip = request.getRemoteAddr();
        return "ip:"+ip;
    }
}
