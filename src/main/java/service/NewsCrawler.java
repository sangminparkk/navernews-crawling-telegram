package service;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

@Getter
public class NewsCrawler {

    private WebDriver webDriver;

    public NewsCrawler() {
        this.webDriver = new ChromeDriver();
    }

    public List<WebElement> fetchNews() {
        //TODO 세션별 관리 필요
        String url = "https://news.naver.com/section/101"; // 경제 세션
        this.webDriver.get(url); // 굳이 열어야하나?

        return this.webDriver.findElements(By.cssSelector("div.section_article._TEMPLATE li"));
    }

}
