package CoinMerge.coinMergeSpring.exchange.repository;

import CoinMerge.coinMergeSpring.exchange.entity.ExchangeConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeConnectionRepository extends JpaRepository<ExchangeConnection, Long> {

}
