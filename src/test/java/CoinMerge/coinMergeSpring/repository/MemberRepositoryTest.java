package CoinMerge.coinMergeSpring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
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
  public void isUserRepositoryNull() {
    assertThat(memberRepository).isNotNull();
  }

  @Test
  @DisplayName("Member 데이터 insert")
  public void createMemberTest() {
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
  @DisplayName("이메일 쿼리 테스트")
  public void duplicateEmailMemberTest() {
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

}
