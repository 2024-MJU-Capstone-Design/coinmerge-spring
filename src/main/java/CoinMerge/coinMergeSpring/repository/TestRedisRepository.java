package CoinMerge.coinMergeSpring.repository;

import CoinMerge.coinMergeSpring.entity.TestSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRedisRepository extends CrudRepository<TestSession, String> {

}
