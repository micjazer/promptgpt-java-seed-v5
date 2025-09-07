
package com.codeiguanas.promptgpt.web.dto;
import java.util.Map;
public class GenerateByIdRequest {
    private Map<String,Object> variables = Map.of();
    private String provider="ECHO"; private String model="echo-1"; private String system="";
    private int expectedOutputTokens = 1000;
    public Map<String,Object> getVariables(){return variables;} public void setVariables(Map<String,Object> v){this.variables=v;}
    public String getProvider(){return provider;} public void setProvider(String v){this.provider=v;}
    public String getModel(){return model;} public void setModel(String v){this.model=v;}
    public String getSystem(){return system;} public void setSystem(String v){this.system=v;}
    public int getExpectedOutputTokens(){return expectedOutputTokens;} public void setExpectedOutputTokens(int v){this.expectedOutputTokens=v;}
}
