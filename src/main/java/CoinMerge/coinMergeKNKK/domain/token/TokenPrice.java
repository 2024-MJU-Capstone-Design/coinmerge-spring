package CoinMerge.coinMergeKNKK.domain.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter @Setter
@Table(name = "TOKEN_PRICE")
public class TokenPrice {
    @Id @GeneratedValue
    @Column(name = "TOKEN_PRICE_ID")
    private String id;

    @Column(name = "TOKEN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Token token;

    @Column(name = "LOWEST_PRICE")
    private int lowestPrice;
    @Column(name = "HIGHEST_PRICE")
    private int highestPrice;
    @Column(name = "TIMESTAMP")
    private Date timeStamp;
    @Column(name = "START_PRICE")
    private int startPrice;
    @Column(name = "CLOSE_PRICE")
    private int closePrice;
    @Column(name = "VOLUME")
    private int volume;
}
