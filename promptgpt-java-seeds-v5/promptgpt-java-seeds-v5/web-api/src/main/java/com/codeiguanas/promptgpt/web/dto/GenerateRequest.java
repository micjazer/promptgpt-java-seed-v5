
package com.codeiguanas.promptgpt.web.dto;
import jakarta.validation.constraints.NotBlank; import java.util.Map;
public class GenerateRequest {
    @NotBlank private String templateContent;
    private Map<String,Object> variables = Map.of();
    private String provider = "ECHO"; private String model = "echo-1"; private String system = "";
    private int expectedOutputTokens = 1000;
    public String getTemplateContent(){return templateContent;} public void setTemplateContent(String v){this.templateContent=v;}
    public Map<String,Object> getVariables(){return variables;} public void setVariables(Map<String,Object> v){this.variables=v;}
    public String getProvider(){return provider;} public void setProvider(String v){this.provider=v;}
    public String getModel(){return model;} public void setModel(String v){this.model=v;}
    public String getSystem(){return system;} public void setSystem(String v){this.system=v;}
    public int getExpectedOutputTokens(){return expectedOutputTokens;} public void setExpectedOutputTokens(int v){this.expectedOutputTokens=v;}
}
