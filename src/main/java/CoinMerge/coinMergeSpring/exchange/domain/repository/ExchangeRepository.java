package CoinMerge.coinMergeSpring.exchange.domain.repository;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

}
