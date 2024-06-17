import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Deque;

public class Bot extends TelegramLongPollingBot {
    //TODO : 실행시간대 OR 실행/종료 조건 추가 (.exe파일로 프로그램 운영되게 할수있나)

    private static final String TOKEN = "put your token";
    private static final String USERNAME = "put your username";

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();

        System.out.println(user.getFirstName() + " : " + message.getText());
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    public void sendText(String who, String what) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(who)
                .text(what)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws TelegramApiException, IOException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        botsApi.registerBot(bot);
        Deque<NewsDto> newsList = new NewsCrawling().getNews(new String[]{"IT/과학", "세계"}); // session 추가 가능
        while (!newsList.isEmpty()) {
            NewsDto news = newsList.pop();
            bot.sendText("put your id", "["+news.getCategory()+"]"+news.getTitle()+"\n"+news.getLink());
        }
    }
}
