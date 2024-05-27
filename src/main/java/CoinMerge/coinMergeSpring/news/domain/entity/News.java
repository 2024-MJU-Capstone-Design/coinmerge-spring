package CoinMerge.coinMergeSpring.news.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "news")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {
    @Id @Column(name = "title", length = 500)
    String title;
    @Column(name = "content", length = 5000)
    String content;
    @Column(name = "link", length = 500)
    String link;
    @Column(name = "date", length = 500)
    String published_at;

    @Builder
    public News(String title, String content, String link, String published_at, String tokenId) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.published_at = published_at;
    }

    @Override
    public String toString() {
        return "newsDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", date='" + published_at + '\'' +
                '}';
    }
}
