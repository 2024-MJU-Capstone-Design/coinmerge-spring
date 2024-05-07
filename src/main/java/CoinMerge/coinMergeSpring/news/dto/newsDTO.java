package CoinMerge.coinMergeSpring.news.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "news")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class newsDTO {
    @Id @Column(name = "title")
    String title;
    @Column(name = "content")
    String content;
    @Column(name = "link")
    String link;
    @Column(name = "date")
    String date;

    public newsDTO(String title, String content, String link, String date) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.date = date;
    }

    @Override
    public String toString() {
        return "newsDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
