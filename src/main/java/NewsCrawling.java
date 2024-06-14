import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsCrawling {

    String url = "https://news.naver.com/section/101";

    public List<String> getCrawling() throws IOException {
        List<String> titleList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        for (Element element : doc.select("li.sa_item._LAZY_LOADING_WRAP")) {
            titleList.add(element.select("strong.sa_text_strong").text());
        }
        return titleList;
    }

    public static void main(String[] args) throws IOException {
        NewsCrawling newsCrawling = new NewsCrawling();
        List<String> list = newsCrawling.getCrawling();
        list.forEach(System.out::println);
    }
}
