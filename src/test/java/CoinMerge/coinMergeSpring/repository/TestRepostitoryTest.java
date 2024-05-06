package CoinMerge.coinMergeSpring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TestRepostitoryTest {

  @Autowired
  private CoinMerge.coinMergeSpring.repository.repository repository;

  @Test
  @DisplayName("테스트 엔티티 설정")
  public void insertTest() {
    entity testEntity;
    entity resultEntity;

    testEntity = entity.builder().password("test").username("test").build();
    resultEntity = this.repository.save(testEntity);

    System.out.println(resultEntity.toString());
  }
}
