package CoinMerge.coinMergeSpring.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
  @Column(length = 36, updatable = false)
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
