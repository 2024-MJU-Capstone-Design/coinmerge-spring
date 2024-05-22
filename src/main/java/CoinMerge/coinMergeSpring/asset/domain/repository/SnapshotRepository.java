package CoinMerge.coinMergeSpring.asset.domain.repository;

import CoinMerge.coinMergeSpring.asset.domain.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
}
