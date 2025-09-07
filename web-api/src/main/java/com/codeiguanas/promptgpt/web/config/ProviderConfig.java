
package com.codeiguanas.promptgpt.web.config;
import com.codeiguanas.promptgpt.providers.ProviderGateway;
import com.codeiguanas.promptgpt.providers.echo.EchoProviderGateway;
import com.codeiguanas.promptgpt.providers.openai.OpenAiProviderGateway;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
@Configuration public class ProviderConfig {
    @Bean public ProviderGateway echoProvider(){ return new EchoProviderGateway(); }
    @Bean public ProviderGateway openAiProvider(){ try { return new OpenAiProviderGateway(); } catch(Exception e){ return new EchoProviderGateway(); } }
    @Bean public ProviderRegistry providerRegistry(ProviderGateway echoProvider, ProviderGateway openAiProvider){ return new ProviderRegistry(echoProvider, openAiProvider); }
}
