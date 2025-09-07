
package com.codeiguanas.promptgpt.domain.lint.rules;
import com.codeiguanas.promptgpt.domain.lint.LintFinding; import com.codeiguanas.promptgpt.domain.lint.LintRule;
public final class AudienceRule implements LintRule {
    public LintFinding apply(String renderedPrompt) {
        String lp = renderedPrompt.toLowerCase();
        boolean ok = lp.contains("pour ") || lp.contains("for ");
        return ok ? LintFinding.ok("Audience spécifiée") : LintFinding.warn("Audience absente : précisez à qui s'adresse la réponse.");
    }
    public String code(){return "AUDIENCE_PRESENT";}
    public String description(){return "Le prompt doit expliciter l'audience.";}
}
