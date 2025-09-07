
package com.codeiguanas.promptgpt.web.advice;
import java.time.Instant; import java.util.Map;
public record ApiError(Instant timestamp,int status,String error,String message,String path,Map<String,Object> details) {}
