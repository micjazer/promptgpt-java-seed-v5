
package com.codeiguanas.promptgpt.domain;

import java.time.Instant;
import java.util.*;

public final class PromptTemplate {
    private final String id, name, description, language, audience, tone, format, content, createdBy;
    private final Set<String> tags;
    private final List<TemplateVariable> variables;
    private final Instant createdAt;
    public PromptTemplate(String id, String name, String description, String language, String audience, String tone,
                          String format, Set<String> tags, List<TemplateVariable> variables, String content, String createdBy) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.language = Objects.requireNonNull(language);
        this.audience = Objects.requireNon(audience);
        this.tone = Objects.requireNonNull(tone);
        this.format = Objects.requireNonNull(format);
        this.tags = tags == null ? Set.of() : Set.copyOf(tags);
        this.variables = variables == null ? List.of() : List.copyOf(variables);
        this.content = Objects.requireNonNull(content);
        this.createdAt = Instant.now();
        this.createdBy = Objects.requireNonNull(createdBy);
    }
    public String id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public String language() { return language; }
    public String audience() { return audience; }
    public String tone() { return tone; }
    public String format() { return format; }
    public Set<String> tags() { return tags; }
    public List<TemplateVariable> variables() { return variables; }
    public String content() { return content; }
    public Instant createdAt() { return createdAt; }
    public String createdBy() { return createdBy; }
}
