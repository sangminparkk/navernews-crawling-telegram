import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class NewsCrawling {

    private final String URL = "https://news.naver.com";
    private Map<String, String> mapUrl = new HashMap<>();

    //TODO : 선택적 크롤링 (내가 원하는 키워드가 들어간 뉴스만 크롤링)
    public void getSessionUrl() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        Elements elements = doc.select("li.Nlist_item");
        for (Element element : elements) {
            mapUrl.put(element.select("a span.Nitem_link_menu").text(), element.select("a[href]").attr("abs:href"));
        }
    }

    public Deque<NewsDto> getNews(String[] categoryArray) throws IOException {
        Deque<NewsDto> deque = new ArrayDeque<>();
        getSessionUrl(); // TODO : 여기서 추가로 선언하는게 맞을까? 불필요한 것으로 보임

        for (String category : categoryArray) {
            String targetUrl = this.mapUrl.get(category);

            if (targetUrl == null) {
                System.out.println("카테고리의 url 을 찾을 수 없습니다");
                continue;
            }

            Document doc = Jsoup.connect(targetUrl).get();
            Elements newsElements = doc.select("a.sa_text_title");
            if (newsElements.isEmpty()) {
                System.out.println("a.sa_text_title 요소를 찾을 수 없습니다");
            }

            for (Element element : newsElements) {
                deque.add(NewsDto.builder()
                        .link(element.select("a[href]").attr("href"))
                        .title(element.select("strong.sa_text_strong").text())
                        .category(category)
                        .build());
            }
        }
        return deque;
    }

    public static void main(String[] args) throws IOException {
        NewsCrawling crawling3 = new NewsCrawling();
        Deque<NewsDto> deque = crawling3.getNews(new String[]{"IT/과학", "세계"}); // {"정치", "경제", "IT/과학", "세계"}
        while (!deque.isEmpty()) {
            NewsDto pop = deque.pop();
            System.out.println("["+pop.getCategory()+"]"+ pop.getTitle() + " " + pop.getLink());
        }
    }

}
