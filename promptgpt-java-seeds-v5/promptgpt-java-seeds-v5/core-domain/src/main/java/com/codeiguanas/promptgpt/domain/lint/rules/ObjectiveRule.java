
package com.codeiguanas.promptgpt.domain.lint.rules;
import com.codeiguanas.promptgpt.domain.lint.LintFinding; import com.codeiguanas.promptgpt.domain.lint.LintRule;
public final class ObjectiveRule implements LintRule {
    public LintFinding apply(String renderedPrompt) {
        String lp = renderedPrompt.toLowerCase();
        boolean ok = lp.startsWith("rédige ")||lp.startsWith("explique ")||lp.matches("^\s*write\b.*")||lp.matches("^\s*explain\b.*");
        return ok ? LintFinding.ok("Objectif clair") : LintFinding.warn("Objectif ambigu : commencez par un verbe d'action.");
    }
    public String code(){return "OBJECTIVE_ACTION_VERB";}
    public String description(){return "Commencer par un verbe d'action clarifie la mission.";}
}
