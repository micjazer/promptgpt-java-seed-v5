
package com.codeiguanas.promptgpt.providers.echo;
import com.codeiguanas.promptgpt.providers.*;
import java.nio.charset.StandardCharsets; import java.util.Map;
public final class EchoProviderGateway implements ProviderGateway {
    public GenerationResult invoke(String model, String system, String prompt, Map<String,Object> parameters) {
        long start=System.nanoTime(); String text="[ECHO]["+model+"] "+(system==null?"":"<SYS> "+system+" </SYS> ")+prompt;
        long latency=(System.nanoTime()-start)/1_000_000L;
        int tokensIn=Math.max(1,prompt.getBytes(StandardCharsets.UTF_8).length/4);
        int tokensOut=Math.max(1,text.getBytes(StandardCharsets.UTF_8).length/4);
        return new GenerationResult(text,tokensIn,tokensOut,latency);
    }
    public ProviderType type(){return ProviderType.ECHO;}
    @Override
    public void stream(String model,String system,String prompt,Map<String,Object> parameters,
                       java.util.function.Consumer<String> onDelta,
                       java.util.function.Consumer<Map<String,Object>> onUsage){
        long start=System.nanoTime(); String text="[ECHO]["+model+"] "+(system==null?"":"<SYS> "+system+" </SYS> ")+prompt;
        int chunk=128; for(int i=0;i<text.length();i+=chunk){ onDelta.accept(text.substring(i,Math.min(text.length(),i+chunk))); }
        long latency=(System.nanoTime()-start)/1_000_000L;
        int tokensIn=Math.max(1,prompt.getBytes(StandardCharsets.UTF_8).length/4);
        int tokensOut=Math.max(1,text.getBytes(StandardCharsets.UTF_8).length/4);
        onUsage.accept(Map.of("tokensIn",tokensIn,"tokensOut",tokensOut,"latencyMs",latency));
    }
}
