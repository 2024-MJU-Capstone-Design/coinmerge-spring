package CoinMerge.coinMergeSpring.member.domain.repository;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
  Member findByEmail(final String email);
  Member findByNickname(final String nickname);
}
