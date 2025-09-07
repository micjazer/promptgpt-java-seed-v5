
package com.codeiguanas.promptgpt.web.config;
import com.codeiguanas.promptgpt.providers.*; import java.util.*;
public class ProviderRegistry implements ProviderGateway {
    private final Map<ProviderType, ProviderGateway> byType=new EnumMap<>(ProviderType.class);
    public ProviderRegistry(ProviderGateway...g){ for (ProviderGateway x:g) byType.put(x.type(), x); }
    public ProviderGateway get(ProviderType t){ return byType.get(t); }
    public GenerationResult invoke(String m,String s,String p,java.util.Map<String,Object> par){ throw new UnsupportedOperationException(); }
    public ProviderType type(){ return null; }
}
