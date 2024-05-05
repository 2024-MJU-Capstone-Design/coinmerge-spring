package CoinMerge.coinMergeSpring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  public void 멤버_생성() {
    //given
    Member testUser = new Member().builder().email("test@test.com").password("test password")
        .salt("test salt").nickname("닉네임").profileImageUri("프로필").description("설명").build();
    //when
    Member result = memberRepository.save(testUser);
    //then
    System.out.println(result);
    assertThat(result.getId().length()).isEqualTo(36);
    assertThat(result.getPassword()).isEqualTo("test password");
    assertThat(result.getSalt()).isEqualTo("test salt");
  }

  @Test
  public void 이메일_중복검사() {
    // given
    final Member user = Member.builder().email("test@test.com").password("test").salt("test")
        .nickname("앤디").description("설명").profileImageUri("프로필").build();
    memberRepository.save(user);

    // when
    final Member result = memberRepository.findByEmail("test@test.com");

    // then
    assertThat(result).isNotNull();
    assertThat(result.getPassword()).as("test");
    assertThat(result.getEmail()).as("test@test.com");
    assertThat(result.getSalt()).as("test");
  }

  @Test
  public void 닉네임_중복검사() {
    // given
    final Member user = Member.builder().email("test@test.com").password("test").salt("test")
        .nickname("앤디").description("설명").profileImageUri("프로필").build();
    memberRepository.save(user);

    // when
    final Member result = memberRepository.findByNickname("앤디");

    // then
    assertThat(result).isNotNull();
    assertThat(result.getNickname()).isEqualTo("앤디");
  }

  @Test
  public void 멤버_삭제() {
    // given
    final String uuid = UUID.randomUUID().toString();
    final Member member = Member.builder().id(uuid).email("test@test.com").password("test password")
        .salt("test salt").nickname("tester").profileImageUri("test profile").build();

    // when
    final Member savedMember = memberRepository.save(member);
    memberRepository.deleteById(savedMember.getId());

    // then
    final Optional<Member> searchedMember = memberRepository.findById(savedMember.getId());
    assertThat(searchedMember).isEmpty();
  }

}
