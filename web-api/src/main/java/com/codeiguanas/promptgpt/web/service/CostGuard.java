
package com.codeiguanas.promptgpt.web.service;
import com.codeiguanas.promptgpt.domain.TokenEstimator; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import java.util.Map;
@Service public class CostGuard {
    private static final Map<String,Double> IN = Map.of("gpt-4o",5.00,"gpt-4o-mini",0.60);
    private static final Map<String,Double> OUT= Map.of("gpt-4o",20.00,"gpt-4o-mini",2.40);
    private final double maxCostUsd;
    public CostGuard(@Value("${promptgpt.cost.max-usd:0.10}") double maxCostUsd){ this.maxCostUsd=maxCostUsd; }
    public void guard(String model,String system,String prompt,int expectedOutputTokens){
        int inTok = TokenEstimator.estimate(model,(system==null?"":system)+"\n"+prompt);
        int outTok = Math.max(1, expectedOutputTokens);
        double inP = price(IN,model), outP = price(OUT,model);
        double est = (inTok*inP + outTok*outP)/1_000_000.0;
        if (est>maxCostUsd) throw new IllegalStateException(String.format("Estimated cost %.4f USD exceeds guardrail %.4f USD (model=%s, inTok=%d, outTok=%d)",est,maxCostUsd,model,inTok,outTok));
    }
    private double price(Map<String,Double> m,String model){ if(model==null)return m.getOrDefault("gpt-4o-mini",1.0); String k=model.toLowerCase(); if(k.startsWith("gpt-4o-mini")) return m.getOrDefault("gpt-4o-mini",1.0); if(k.startsWith("gpt-4o")) return m.getOrDefault("gpt-4o",5.0); return m.getOrDefault("gpt-4o-mini",1.0); }
}
