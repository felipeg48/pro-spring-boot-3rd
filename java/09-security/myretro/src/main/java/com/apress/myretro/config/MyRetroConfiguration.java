package com.apress.myretro.config;


import com.apress.myretro.board.Card;
import com.apress.myretro.board.CardType;
import com.apress.myretro.board.RetroBoard;
import com.apress.myretro.service.RetroBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@EnableConfigurationProperties({MyRetroProperties.class})
@Configuration
public class MyRetroConfiguration {

    @Bean
    ApplicationListener<ApplicationReadyEvent> ready(RetroBoardService retroBoardService) {
        return applicationReadyEvent -> {
            log.info("Application Ready Event");
            UUID retroBoardId = UUID.fromString("9dc9b71b-a07e-418b-b972-40225449aff2");
            retroBoardService.save(RetroBoard.builder()
                    .id(retroBoardId)
                    .name("Spring Boot Conference 2023")
                    .card(Card.builder().id(UUID.fromString("bb2a80a5-a0f5-4180-a6dc-80c84bc014c9")).comment("Spring Boot Rocks!").cardType(CardType.HAPPY).build())
                    .card(Card.builder().id(UUID.randomUUID()).comment("Meet everyone in person").cardType(CardType.HAPPY).build())
                    .card(Card.builder().id(UUID.randomUUID()).comment("When is the next one?").cardType(CardType.MEH).build())
                    .card(Card.builder().id(UUID.randomUUID()).comment("Not enough time to talk to everyone").cardType(CardType.SAD).build())
                    .build()).subscribe();
        };
    }

}
