package controller;

import entity.NewsDto;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private static final String TOKEN = "";
    private static final String USERNAME = "";
    private static final String CHAT_ID = "";

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    // 현재 나한텐 필요없는 기능 같음
    //TODO: 메세지 입력 disable 처리 필요
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
//        System.out.println("user = " + user.getFirstName() + " wrote " + message.getText());
    }

    public void sendText(NewsDto newsDto) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(CHAT_ID)
                .text(newsDto.getTitle() + "\n" + newsDto.getLink())
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}