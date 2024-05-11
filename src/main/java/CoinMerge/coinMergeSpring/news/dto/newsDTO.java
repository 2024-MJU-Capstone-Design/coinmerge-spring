package CoinMerge.coinMergeSpring.news.dto;

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
public class newsDTO {
    @Id @Column(name = "title")
    String title;
    @Column(name = "content")
    String content;
    @Column(name = "link")
    String link;
    @Column(name = "date")
    String published_at;

    @Column(name = "tokenId")
    String tokenId;

    @Builder
    public newsDTO(String title, String content, String link, String published_at, String tokenId) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.published_at = published_at;
        this.tokenId = tokenId;
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
