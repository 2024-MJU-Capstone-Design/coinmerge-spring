package CoinMerge.coinMergeSpring.asset.domain.repository;

import CoinMerge.coinMergeSpring.asset.domain.entity.DepositAndWithdraw;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositWithdrawRepository extends JpaRepository<DepositAndWithdraw, Long> {

    boolean exists(DepositAndWithdraw example);

    @Override
    DepositAndWithdraw save(DepositAndWithdraw entity);


}
