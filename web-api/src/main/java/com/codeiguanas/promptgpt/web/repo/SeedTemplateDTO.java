
package com.codeiguanas.promptgpt.web.repo;
import java.util.List; import java.util.Set;
public class SeedTemplateDTO {
    public String id, language, audience, domain, role, format, tone, name, description, content, system;
    public Set<String> tags; public List<VariableDTO> variables;
    public static class VariableDTO { public String name, type, description, defaultValue; public boolean required=false; }
}
