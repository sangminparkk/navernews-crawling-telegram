import AppConfig.DatabaseManager;
import controller.TelegramBot;
import entity.NewsDto;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import repository.NewsRepositoryImpl;
import service.NewsCrawler;
import service.NewsService;

import java.util.List;

public class Main {
    public static void main(String[] args){
        TelegramBotsApi botsApi = null;

        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot bot = new TelegramBot();
            botsApi.registerBot(bot);
            NewsService newsService = new NewsService(new NewsCrawler(), new NewsRepositoryImpl(new DatabaseManager()));
            List<NewsDto> newsList = newsService.sendNews();
            for (NewsDto news : newsList) {
                bot.sendText(news);
            }

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
