package CoinMerge.coinMergeSpring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Table(name = "test")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestEntity {
  @Id
  private String username;
  private String password;

  @Builder
  public TestEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
