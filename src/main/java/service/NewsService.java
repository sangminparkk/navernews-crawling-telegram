package service;

import entity.NewsDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsService {

    private NewsCrawler newsCrawler;

    public NewsService() {
        this.newsCrawler = new NewsCrawler();
    }

    public List<NewsDto> sendNews() {
        List<NewsDto> newsList = new ArrayList<>();
        NewsDto newsDto = new NewsDto();
        Elements newsItems = this.newsCrawler.fetchNews();
        for (Element newsItem : newsItems) {
            newsList.add(NewsDto.builder()
                    .title(newsItem.select("strong.sa_text_strong").text())
                    .link(newsItem.select("a[href]").attr("href"))
                    .build());
        }
        return newsList;
    }
}
