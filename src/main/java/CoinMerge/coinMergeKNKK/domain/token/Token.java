package CoinMerge.coinMergeKNKK.domain.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "TOKEN")
public class Token {

    @Id @GeneratedValue
    @Column(name = "TOKEN_ID")
    private Long id;

    @Column(name = "TOKEN_NAME")
    private String name;
    @Column(name = "TOKEN_LOGO")
    private String logo;

}
