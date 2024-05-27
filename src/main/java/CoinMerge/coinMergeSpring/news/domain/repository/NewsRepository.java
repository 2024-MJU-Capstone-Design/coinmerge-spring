package CoinMerge.coinMergeSpring.news.domain.repository;

import CoinMerge.coinMergeSpring.news.domain.entity.News;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{
    News save(News newsDTO);
    Optional<News> findById(Long id);
    boolean existsByTitle(String title);
}
