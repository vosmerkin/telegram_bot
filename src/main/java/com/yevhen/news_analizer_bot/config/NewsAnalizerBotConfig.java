package com.yevhen.news_analizer_bot.config;

import com.yevhen.news_analizer_bot.bot.NewsAnalizerBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class NewsAnalizerBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(NewsAnalizerBot newsAnalizerBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(newsAnalizerBot);
        return api;
    }
}
