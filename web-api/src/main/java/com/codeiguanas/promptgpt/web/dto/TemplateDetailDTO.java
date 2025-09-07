
package com.codeiguanas.promptgpt.web.dto;
import com.codeiguanas.promptgpt.domain.TemplateVariable; import java.util.List; import java.util.Set;
public record TemplateDetailDTO(String id,String name,String description,String language,String audience,String tone,String format,Set<String> tags,List<TemplateVariable> variables,String content) { }
