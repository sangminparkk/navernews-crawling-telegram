import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class NewsCrawling {

    private String url = "https://news.naver.com/section/101";

    public Deque<NewsDto> getCrawling() throws IOException {
        Deque<NewsDto> deque = new ArrayDeque<>();
        Document doc = Jsoup.connect(url).get();

        for (Element element : doc.select("a.sa_text_title")) {
            deque.add(NewsDto.builder()
                    .link(element.select("a[href]").attr("href"))
                    .title(element.select("strong.sa_text_strong").text())
                    .build());
        }
        return deque;
    }

    public static void main(String[] args) throws IOException {
        NewsCrawling newsCrawling = new NewsCrawling();
        Deque<NewsDto> crawling = newsCrawling.getCrawling();

        while (!crawling.isEmpty()) {
            NewsDto news = crawling.pop();
            System.out.println(news.getTitle() + "\n" + news.getLink());
            break;
        }
    }
}
