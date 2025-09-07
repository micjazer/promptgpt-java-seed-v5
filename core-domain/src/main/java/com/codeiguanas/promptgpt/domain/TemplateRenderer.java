
package com.codeiguanas.promptgpt.domain;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.*;

public final class TemplateRenderer {
    private final MustacheFactory mf = new DefaultMustacheFactory();
    public String render(String content, Map<String,Object> vars) {
        Objects.requireNonNull(content);
        Objects.requireNonNull(vars);
        Set<String> missing = findMissingVariables(content, vars.keySet());
        if (!missing.isEmpty()) throw new IllegalArgumentException("Variables manquantes: " + missing);
        Mustache m = mf.compile(new StringReader(content), "inline");
        StringWriter out = new StringWriter();
        m.execute(out, vars);
        return out.toString();
    }
    public Set<String> findMissingVariables(String content, Set<String> provided) {
        Pattern p = Pattern.compile("\{\{\s*([a-zA-Z0-9_\.]+)\s*}}");
        Matcher m = p.matcher(content);
        Set<String> required = new HashSet<>();
        while (m.find()) required.add(m.group(1));
        required.removeAll(provided);
        return required;
    }
}
