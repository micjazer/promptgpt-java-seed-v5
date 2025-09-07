
package com.codeiguanas.promptgpt.providers.openai;

import com.codeiguanas.promptgpt.providers.*;
import com.fasterxml.jackson.databind.*;
import java.net.*; import java.net.http.*; import java.nio.charset.StandardCharsets; import java.time.Duration; import java.util.Map;

public final class OpenAiProviderGateway implements ProviderGateway {
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    private final ObjectMapper om = new ObjectMapper();
    private final String apiKey; private final String url;
    public OpenAiProviderGateway() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        if (this.apiKey == null || this.apiKey.isBlank()) throw new IllegalStateException("OPENAI_API_KEY is not set");
        this.url = System.getenv().getOrDefault("OPENAI_API_URL","https://api.openai.com/v1/responses");
    }
    public ProviderType type(){ return ProviderType.OPENAI; }

    public GenerationResult invoke(String model, String system, String prompt, Map<String,Object> parameters) {
        try {
            String body = om.writeValueAsString(Map.of(
                    "model", model,
                    "input", new Object[]{
                            Map.of("role","system","content", new Object[]{ Map.of("type","text","text", system==null?"":system)}),
                            Map.of("role","user","content", new Object[]{ Map.of("type","text","text", prompt)})
                    }
            ));
            long start=System.nanoTime();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url)).timeout(Duration.ofSeconds(60))
                    .header("Authorization","Bearer "+apiKey).header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            long latency=(System.nanoTime()-start)/1_000_000L;
            if (resp.statusCode()/100!=2) throw new RuntimeException("OpenAI error HTTP "+resp.statusCode()+": "+resp.body());
            JsonNode node = om.readTree(resp.body());
            String text = node.has("output_text") ? node.get("output_text").asText() : resp.body();
            int inTok = node.path("usage").path("input_tokens").asInt(0);
            int outTok = node.path("usage").path("output_tokens").asInt(0);
            return new GenerationResult(text,inTok,outTok,latency);
        } catch(Exception e){ throw new RuntimeException("OpenAI invocation failed: "+e.getMessage(),e); }
    }

    @Override
    public void stream(String model,String system,String prompt,Map<String,Object> parameters,
                       java.util.function.Consumer<String> onDelta,
                       java.util.function.Consumer<Map<String,Object>> onUsage) {
        try {
            String body = om.writeValueAsString(Map.of(
                    "model", model,
                    "stream", true,
                    "input", new Object[]{
                            Map.of("role","system","content", new Object[]{ Map.of("type","text","text", system==null?"":system)}),
                            Map.of("role","user","content", new Object[]{ Map.of("type","text","text", prompt)})
                    }
            ));
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url)).timeout(Duration.ofSeconds(300))
                    .header("Authorization","Bearer "+apiKey)
                    .header("Content-Type","application/json")
                    .header("Accept","text/event-stream")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
            long start=System.nanoTime();
            HttpResponse<java.io.InputStream> resp = http.send(req, HttpResponse.BodyHandlers.ofInputStream());
            if (resp.statusCode()/100!=2) {
                String err = new String(resp.body().readAllBytes(), StandardCharsets.UTF_8);
                throw new RuntimeException("OpenAI stream HTTP "+resp.statusCode()+": "+err);
            }
            try (var in = resp.body(); var isr = new java.io.InputStreamReader(in, StandardCharsets.UTF_8); var br = new java.io.BufferedReader(isr)) {
                String line, currentEvent=""; int inTok=0, outTok=0;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) continue;
                    if (line.startsWith("event:")) { currentEvent = line.substring(6).trim(); continue; }
                    if (line.startsWith("data:")) {
                        String data = line.substring(5).trim();
                        if ("[DONE]".equals(data)) {
                            long latency=(System.nanoTime()-start)/1_000_000L;
                            onUsage.accept(Map.of("tokensIn",inTok,"tokensOut",outTok,"latencyMs",latency));
                            break;
                        }
                        JsonNode node = om.readTree(data);
                        String ev = (currentEvent==null||currentEvent.isEmpty()) ? node.path("event").asText("") : currentEvent;
                        if ("response.output_text.delta".equals(ev)) {
                            String delta = node.path("delta").asText("");
                            if (!delta.isEmpty()) onDelta.accept(delta);
                        } else if ("response.completed".equals(ev)) {
                            JsonNode usage = node.path("response").path("usage");
                            inTok = usage.path("input_tokens").asInt(inTok);
                            outTok = usage.path("output_tokens").asInt(outTok);
                        } else if ("response.error".equals(ev)) {
                            String msg = node.path("error").path("message").asText("Unknown streaming error");
                            throw new RuntimeException("OpenAI streaming error: "+msg);
                        }
                    }
                }
            }
        } catch(Exception e){ throw new RuntimeException("OpenAI stream failed: "+e.getMessage(),e); }
    }
}
