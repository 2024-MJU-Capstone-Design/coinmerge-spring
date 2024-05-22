package CoinMerge.coinMergeSpring.asset.domain.repository;

import CoinMerge.coinMergeSpring.asset.domain.entity.DepositAndWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositWithdrawRepository extends JpaRepository<DepositAndWithdraw, Long> {

    boolean existsById(String exchangeId);

    DepositAndWithdraw save(DepositAndWithdraw entity);


}
