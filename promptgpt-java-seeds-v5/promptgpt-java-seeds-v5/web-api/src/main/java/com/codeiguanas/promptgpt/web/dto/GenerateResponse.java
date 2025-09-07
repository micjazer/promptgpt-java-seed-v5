
package com.codeiguanas.promptgpt.web.dto;
import com.codeiguanas.promptgpt.domain.lint.LintFinding; import java.util.List;
public record GenerateResponse(String renderedPrompt,String resultText,int tokensIn,int tokensOut,long latencyMs,List<LintFinding> lint) { }
