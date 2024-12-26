package com.yevhen.news_analizer_bot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class NewsAnalizerBot extends TelegramLongPollingBot {
    public static final Logger LOG = LoggerFactory.getLogger(NewsAnalizerBot.class);

    public static final String START = "/start";
    public static final String ADD_STRING_TO_FILTER = "/add";
    public static final String REMOVE_STRING_FROM_FILTER = "/remove";
    public static final String LIST_STRINGS = "/list";


    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.channel.id}")
    private String channelId;

    @Autowired
    private FileService fileService;

    public NewsAnalizerBot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        } else {
            var message = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            switch (message) {
                case START -> {
                    String userName = update.getMessage().getChat().getUserName();
                    startCommand(chatId, userName);
                }
                case ADD_STRING_TO_FILTER -> addLine(chatId);
                case REMOVE_STRING_FROM_FILTER -> remove(chatId);
                case LIST_STRINGS -> {
                    try {
                        list(chatId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> sendMessage(chatId, "UNKNOWN");
            }
        }
        if (update.hasChannelPost()) {
            String messageText = update.getChannelPost().getText();
            Long chatId = update.getChannelPost().getChatId();

            System.out.println("Message from channel: " + messageText);

            // Optional: Process or respond to the message
            if (chatId.toString().equals(channelId)) {
                System.out.println("Processing channel message...");
                // Your logic here
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                /add
                /remove
                /list
                """;
        sendMessage(chatId, text);
    }

    private void addLine(Long chatId){

    }

    private void remove(Long chatId){

    }

    private void list(Long chatId) throws IOException {
        fileService.readLines().forEach(line -> sendMessage(chatId, line));
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
