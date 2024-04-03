package CoinMerge.coinMergeKNKK.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "PROFILE")
public class Profile {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "USER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "NICKNAME")
    private String nickName;
    @Column(name = "PROFILE_IMAGE_URL")
    private String profile_image_url;
    @Column(name = "DESCRIPTION")
    private String description;

}
