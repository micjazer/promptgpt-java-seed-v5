
package com.codeiguanas.promptgpt.domain;

import java.util.Objects;

public final class TemplateVariable {
    public enum Type { STRING, NUMBER, ENUM, DATE }
    private final String name;
    private final Type type;
    private final boolean required;
    private final String defaultValue;
    private final String description;
    public TemplateVariable(String name, Type type, boolean required, String defaultValue, String description) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.required = required;
        this.defaultValue = defaultValue;
        this.description = description;
    }
    public String name() { return name; }
    public Type type() { return type; }
    public boolean required() { return required; }
    public String defaultValue() { return defaultValue; }
    public String description() { return description; }
}
