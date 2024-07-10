package service;

import entity.NewsDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;

public class NewsService {

    private NewsCrawler newsCrawler;
    private NewsRepository newsRepository;

    public NewsService(NewsCrawler newsCrawler, NewsRepository newsRepository) {
        this.newsCrawler = newsCrawler;
        this.newsRepository = newsRepository;
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
