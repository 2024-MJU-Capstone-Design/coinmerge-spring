package CoinMerge.coinMergeSpring.news.domain.repository;

import CoinMerge.coinMergeSpring.news.dto.newsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface newsRepository extends JpaRepository<newsDTO, Long>{
    newsDTO save(newsDTO newsDTO);
    Optional<newsDTO> findById(Long id);
}
