
package com.codeiguanas.promptgpt.web.controller;
import com.codeiguanas.promptgpt.domain.PromptTemplate; import com.codeiguanas.promptgpt.web.dto.*; import com.codeiguanas.promptgpt.web.repo.FileTemplateRepository;
import org.springframework.http.MediaType; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping(path="/api/v1", produces=MediaType.APPLICATION_JSON_VALUE)
public class TemplatesController {
    private final FileTemplateRepository repo;
    public TemplatesController(FileTemplateRepository repo){ this.repo = repo; }
    @GetMapping("/templates") public List<TemplateSummaryDTO> list(@RequestParam Optional<String> q,@RequestParam Optional<String> domain,@RequestParam Optional<String> tag){
        return repo.list(q,domain,tag).stream().map(t->new TemplateSummaryDTO(t.id(),t.name(),t.description(),t.tags())).toList(); }
    @GetMapping("/templates/{id}") public TemplateDetailDTO get(@PathVariable String id){
        PromptTemplate t = repo.get(id).orElseThrow(()->new IllegalArgumentException("Template not found: "+id));
        return new TemplateDetailDTO(t.id(),t.name(),t.description(),t.language(),t.audience(),t.tone(),t.format(),t.tags(),t.variables(),t.content()); }
}
