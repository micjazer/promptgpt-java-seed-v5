
package com.codeiguanas.promptgpt.domain.lint;
import com.codeiguanas.promptgpt.domain.lint.rules.*; import java.util.*;
public final class LintService {
    private final List<LintRule> rules = List.of(new ObjectiveRule(), new AudienceRule(), new FormatRule());
    public List<LintFinding> lint(String renderedPrompt){ List<LintFinding> out=new ArrayList<>(); for (LintRule r:rules) out.add(r.apply(renderedPrompt)); return out; }
}
