package CoinMerge.coinMergeSpring.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "session", timeToLive = 3600)
public class TestSession {
  @Id
  private String id;
  private String sessionId;
}
