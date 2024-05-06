package CoinMerge.coinMergeSpring.repository;

import CoinMerge.coinMergeSpring.entity.repository.redisRepository;
import CoinMerge.coinMergeSpring.entity.session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestSessionRepositoryTest {
  @Autowired
  redisRepository testRedisRepository;

  @Test
  @DisplayName("redis connection test")
  void redisConnectionTest() {
    session session = CoinMerge.coinMergeSpring.entity.session.builder().id("test-id").sessionId("test-session-id").build();
    testRedisRepository.save(session);

    CoinMerge.coinMergeSpring.entity.session findSessionResult = testRedisRepository.findById("test-id").get();
    assertEquals(findSessionResult.getId(), "test-id");
  }
}
