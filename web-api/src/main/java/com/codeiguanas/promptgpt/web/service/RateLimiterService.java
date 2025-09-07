
package com.codeiguanas.promptgpt.web.service;
import io.github.bucket4j.*; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import java.time.Duration; import java.util.Map; import java.util.concurrent.ConcurrentHashMap;
@Service public class RateLimiterService {
    private final Map<String,Bucket> reqs=new ConcurrentHashMap<>(), toks=new ConcurrentHashMap<>();
    private final long rCap,rRef,tCap,tRef;
    public RateLimiterService(@Value("${promptgpt.ratelimit.requests.capacity:60}") long rCap, @Value("${promptgpt.ratelimit.requests.refillPerMinute:60}") long rRef,
                              @Value("${promptgpt.ratelimit.tokens.capacity:10000}") long tCap, @Value("${promptgpt.ratelimit.tokens.refillPerMinute:10000}") long tRef){
        this.rCap=rCap; this.rRef=rRef; this.tCap=tCap; this.tRef=tRef;
    }
    private Bucket newRB(){ return Bucket.builder().addLimit(Bandwidth.classic(rCap, Refill.greedy(rRef, Duration.ofMinutes(1)))).build(); }
    private Bucket newTB(){ return Bucket.builder().addLimit(Bandwidth.classic(tCap, Refill.greedy(tRef, Duration.ofMinutes(1)))).build(); }
    private Bucket rb(String k){ return reqs.computeIfAbsent(k, x->newRB()); }
    private Bucket tb(String k){ return toks.computeIfAbsent(k, x->newTB()); }
    public void consumeRequestOrThrow(String key){ if(!rb(key).tryConsume(1)) throw new com.codeiguanas.promptgpt.web.advice.TooManyRequestsException("Request rate limit exceeded"); }
    public void consumeTokensOrThrow(String key,long tokens){ if(!tb(key).tryConsume(Math.max(1,tokens))) throw new com.codeiguanas.promptgpt.web.advice.TooManyRequestsException("Token rate limit exceeded"); }
}
