
package com.codeiguanas.promptgpt.providers;
import java.util.Map;
public interface ProviderGateway {
    GenerationResult invoke(String model, String system, String prompt, Map<String,Object> parameters);
    ProviderType type();
    /**
     * Optional streaming method: providers may override to push deltas and usage as they arrive.
     */
    default void stream(String model, String system, String prompt, Map<String,Object> parameters,
                        java.util.function.Consumer<String> onDelta,
                        java.util.function.Consumer<java.util.Map<String,Object>> onUsage) {
        throw new UnsupportedOperationException("Streaming not supported by this provider");
    }
}
