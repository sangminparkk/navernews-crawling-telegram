import AppConfig.DatabaseManager;
import controller.TelegramBot;
import entity.NewsDto;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import repository.NewsRepositoryImpl;
import service.NewsCrawler;
import service.NewsService;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args){
        TelegramBotsApi botsApi = null;
        //TODO : DB를 다 지우고 시작해도 될듯? 실시간 뉴스니까

        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot bot = new TelegramBot();
            botsApi.registerBot(bot);

            // DB clear : 변수만 밖으로 빼도 처리가능하구나
            DatabaseManager databaseManager = new DatabaseManager();
            NewsRepositoryImpl newsRepository = new NewsRepositoryImpl(databaseManager);

            log.info(">>>>>> before");
            newsRepository.clearNews();
            log.info(">>>>>> after");

            NewsService newsService = new NewsService(new NewsCrawler(), newsRepository);
            List<NewsDto> newsList = newsService.sendNews();
            for (NewsDto news : newsList) {
                bot.sendText(news);
            }

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
