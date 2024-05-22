package CoinMerge.coinMergeSpring.asset.domain.repository;

import CoinMerge.coinMergeSpring.asset.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository   extends JpaRepository<Transaction, Long> {
    Transaction save(Transaction entity);
    boolean existsById(String exchangeId);
}
