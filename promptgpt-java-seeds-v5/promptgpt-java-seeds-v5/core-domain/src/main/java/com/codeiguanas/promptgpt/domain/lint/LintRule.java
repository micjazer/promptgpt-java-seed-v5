
package com.codeiguanas.promptgpt.domain.lint;
public interface LintRule { LintFinding apply(String renderedPrompt); String code(); String description(); }
