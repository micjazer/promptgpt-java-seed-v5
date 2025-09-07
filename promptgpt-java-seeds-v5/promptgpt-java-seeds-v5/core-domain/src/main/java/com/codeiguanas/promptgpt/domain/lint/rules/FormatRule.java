
package com.codeiguanas.promptgpt.domain.lint.rules;
import com.codeiguanas.promptgpt.domain.lint.LintFinding; import com.codeiguanas.promptgpt.domain.lint.LintRule;
public final class FormatRule implements LintRule {
    public LintFinding apply(String renderedPrompt) {
        String lp = renderedPrompt.toLowerCase();
        boolean ok = lp.contains("liste") || lp.contains("tableau") || lp.contains("code ");
        return ok ? LintFinding.ok("Format précisé") : LintFinding.warn("Format non précisé : indiquez liste, tableau ou code si pertinent.");
    }
    public String code(){return "FORMAT_SPECIFIED";}
    public String description(){return "Spécifier le format de sortie.";}
}
