package com.shivam.aiecommercebackend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        ChatClient client=builder
                .defaultOptions(
                        ChatOptions.builder()
                                .model("qwen2.5:0.5b")
                                .build()
                )
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        return client;
    }
}
