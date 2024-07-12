package service;

import entity.NewsDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsService {

    private NewsCrawler newsCrawler;
    private NewsRepository newsRepository;
    private ScheduledExecutorService scheduler;

    public NewsService(NewsCrawler newsCrawler, NewsRepository newsRepository) {
        this.newsCrawler = newsCrawler;
        this.newsRepository = newsRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
        startScheduler();
    }

    private void startScheduler() {
        Runnable task = this::sendNews;
        this.scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
    }

    private void stopScheduler() {
        scheduler.shutdown();
    }

    public List<NewsDto> sendNews() {
        List<NewsDto> newsList = new ArrayList<>();
        Elements newsItems = this.newsCrawler.fetchNews();

        for (Element newsItem : newsItems) {
            String title = newsItem.select("strong.sa_text_strong").text();
            String link = newsItem.select("a[href]").attr("href");
            NewsDto newsDto = new NewsDto(title, link);

            if (newsRepository.getNewsByLink(link) == null){
                newsRepository.saveNews(newsDto);
            }
            newsList.add(newsDto);
        }
        return newsList;
    }


}
