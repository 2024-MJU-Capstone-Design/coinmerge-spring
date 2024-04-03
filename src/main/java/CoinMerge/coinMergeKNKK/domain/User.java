package CoinMerge.coinMergeKNKK.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "USER")
public class User {
    @Id @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "SALT")
    private String salt;
    @Column(name = "TYPE")
    private String type;//enum?
}
