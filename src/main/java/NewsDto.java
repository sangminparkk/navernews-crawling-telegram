import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDto {

    private String link;
    private String title;
    private String category;


    @Builder
    public NewsDto(String link, String title, String category) {
        this.link = link;
        this.title = title;
        this.category = category;
    }
}
