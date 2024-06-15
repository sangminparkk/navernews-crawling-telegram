import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDto {

    private String link;
    private String title;

    @Builder
    public NewsDto(String link, String title) {
        this.link = link;
        this.title = title;
    }
}
