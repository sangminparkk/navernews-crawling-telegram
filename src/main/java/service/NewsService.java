package service;

import controller.TelegramBot;
import entity.NewsDto;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.NewsRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NewsService {

    private static NewsService instance;
    private NewsCrawler newsCrawler;
    private NewsRepository newsRepository;
    private ScheduledExecutorService scheduler;
    private TelegramBot telegramBot;

    public static synchronized NewsService getInstance(NewsCrawler newsCrawler, NewsRepository newsRepository) {
        if (instance == null) {
            instance = new NewsService(newsCrawler, newsRepository);
        }
        return instance;
    }

    public NewsService(NewsCrawler newsCrawler, NewsRepository newsRepository) {
        this.newsCrawler = newsCrawler;
        this.newsRepository = newsRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.telegramBot = new TelegramBot();
        startScheduler();
    }

    private void startScheduler() {
        Runnable task = this::sendNews;
        this.scheduler.scheduleWithFixedDelay(task, 0, 10, TimeUnit.SECONDS); // 메소드 수행시간 > 스케쥴러
    }

    private void stopScheduler() {
        scheduler.shutdown();
    }

    public List<NewsDto> sendNews() {
        log.info("스케쥴러 실행 : 크롤링 작업 시작");
        WebDriver webDriver = this.newsCrawler.getWebDriver();

        webDriver.navigate().refresh();

        long start = System.currentTimeMillis();

        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.Nicon_service"))); >> 이걸로 시간 많이 잡아먹음. 분석필요.
        } catch (Exception e) {
            log.error("페이지 로딩 중 오류 발생 : " + e.getMessage());
        }

        long end = System.currentTimeMillis();
        long loadedTime = end - start;
        log.info("loadedTime = " + loadedTime);

        List<NewsDto> newsList = new ArrayList<>();
        List<WebElement> articles = this.newsCrawler.fetchNews();

        for (WebElement article : articles) {
            try {
                String title = article.findElement(By.cssSelector("strong.sa_text_strong")).getText();
                String link = article.findElement(By.cssSelector("a")).getAttribute("href");
                NewsDto newsDto = new NewsDto(title, link);

                if (newsRepository.getNewsByLink(link) == null) {
                    newsRepository.saveNews(newsDto);
                }
                newsList.add(newsDto);
                this.telegramBot.sendText(newsDto);
            } catch (Exception e) {
                log.error("sendNews 처리 중 에러 발생 : " +  e.getMessage());
            }
        }
        log.info("sendNews 메소드 종료 >>>> ");

        return newsList;
    }


}
