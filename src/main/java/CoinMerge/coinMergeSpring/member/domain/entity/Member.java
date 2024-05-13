package CoinMerge.coinMergeSpring.member.domain.entity;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.exchange.domain.entity.ExchangeConnection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, name="member_id")
  private String id;

  @Column(length = 64, nullable = false, unique = true, updatable = false)
  private String email;

  @Column(length = 128, nullable = false)
  private String password;

  @Column(length = 128, nullable = false, updatable = false)
  private String salt;

  @Column(length = 32)
  private String nickname;

  @Column(name = "profile_image_uri")
  private String profileImageUri;

  private String description;

  @CreationTimestamp
  @Column(length = 20, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(length = 20)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<ExchangeConnection> exchangeConnectionList;

  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<Asset> assetList;

  public Member(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Member{" +
        "id='" + id + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", salt='" + salt + '\'' +
        ", nickname='" + nickname + '\'' +
        ", profileImageUri='" + profileImageUri + '\'' +
        ", description='" + description + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
