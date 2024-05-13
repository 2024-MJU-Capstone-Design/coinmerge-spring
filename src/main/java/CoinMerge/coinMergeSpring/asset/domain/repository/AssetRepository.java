package CoinMerge.coinMergeSpring.asset.domain.repository;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

}
