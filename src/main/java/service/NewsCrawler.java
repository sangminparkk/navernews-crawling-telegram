package service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsCrawler {
    public Elements fetchNews() {
        //TODO 세션별 관리 필요
        String url = "https://news.naver.com/section/101"; // 경제 세션
        Connection connect = Jsoup.connect(url);

        try {
            Document document = connect.get();
            return document.select("div.section_article._TEMPLATE li");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
