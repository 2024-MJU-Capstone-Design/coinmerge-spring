package CoinMerge.coinMergeSpring.exchange.repository;

import CoinMerge.coinMergeSpring.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

}