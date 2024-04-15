package CoinMerge.coinMergeSpring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

  private Member member;
  private MemberDto memberDto;
  @InjectMocks
  private MemberService target;
  @Mock
  private MemberRepository memberRepository;

  @BeforeEach
  public void init() {
    memberDto = MemberDto.builder().nickname("andy").email("test@test.com")
        .password("password").build();
    member = Member.builder().nickname("andy").email("test@test.com")
        .password("password").build();
  }

  @Test
  @DisplayName("중복 이메일 유저 회원가입 시도")
  public void alreadyExistEmail() {
    final MemberDto duplicateMemberDto = MemberDto.builder().email("test@test.com").password("test").description("test").nickname("test")
        .profileImageUri("test").build();
    // given
    doReturn(Member.builder().build()).when(memberRepository).findByEmail(duplicateMemberDto.getEmail());

    // when
    final MemberException result = assertThrows(MemberException.class,
        () -> target.signUp(duplicateMemberDto));

    // then
    assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.DUPLICATE_MEMBER_REGISTER);
  }

  @Test
  @DisplayName("회원가입 성공")
  public void successSignUp() throws Exception {
    //given
    doReturn(null).when(memberRepository).findByEmail(memberDto.getEmail());
    doReturn(member).when(memberRepository).save(any(Member.class));

    //when
    final MemberResponse memberResult = target.signUp(memberDto);

    //then
    assertThat(memberResult.getEmail()).isEqualTo(memberDto.getEmail());

    //verify
    verify(memberRepository, times(1)).findByEmail(memberResult.getEmail());
    verify(memberRepository, times(1)).save(any(Member.class));
  }
}
