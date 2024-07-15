package entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDto {

    private String link;
    private String title;

    @Builder
    public NewsDto(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
