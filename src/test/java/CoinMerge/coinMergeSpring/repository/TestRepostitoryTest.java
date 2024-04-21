package CoinMerge.coinMergeSpring.repository;

import CoinMerge.coinMergeSpring.entity.TestEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TestRepostitoryTest {

  @Autowired
  private TestRepository repository;

  @Test
  @DisplayName("테스트 엔티티 설정")
  public void insertTest() {
    TestEntity testEntity;
    TestEntity resultEntity;

    testEntity = TestEntity.builder().password("test").username("test").build();
    resultEntity = this.repository.save(testEntity);

    System.out.println(resultEntity.toString());
  }
}
