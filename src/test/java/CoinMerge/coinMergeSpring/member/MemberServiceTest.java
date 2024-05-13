package CoinMerge.coinMergeSpring.member;

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
import CoinMerge.coinMergeSpring.member.dto.MemberUpdateRequest;
import CoinMerge.coinMergeSpring.member.exception.DuplicateNicknameException;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.service.MemberService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

  private Member member;
  private String memberId;
  private MemberDto memberDto;
  @InjectMocks
  private MemberService target;
  @Mock
  private MemberRepository memberRepository;

  @BeforeEach
  public void init() {
    memberId = UUID.randomUUID().toString();

    memberDto = MemberDto.builder().nickname("andy").email("test@test.com")
        .password("password").build();
    member = Member.builder().id(memberId).salt("test").nickname("andy").email("test@test.com")
        .password("password").build();
  }

  @Test
  public void 중복_이메일_유저_회원가입_시도() {
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
  public void 회원가입_성공() throws Exception {
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

  @Test
  public void 닉네임_중복_존재() {
    // given
    doReturn(member).when(memberRepository).findByNickname("앤디");

    // when
    final DuplicateNicknameException result = assertThrows(DuplicateNicknameException.class,
        () -> target.checkNickname("앤디"));

    // then
    assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.DUPLICATE_NICKNAME);
  }

  @Test
  public void 닉네임_중복_없음() {
    // given
    doReturn(null).when(memberRepository).findByNickname("앤디");

    // when
    final boolean result = target.checkNickname("앤디");

    // then
    assertThat(result).isTrue();
  }

  @Test
  public void 회원탈퇴실패_존재하지않음() {
    // given
    doReturn(Optional.empty()).when(memberRepository).findById(member.getId());

    // when
    final MemberException memberException = assertThrows(MemberException.class, () -> target.unregist(member.getId()));

    // then
    assertThat(memberException.getErrorResult()).isEqualTo(MemberErrorResult.MEMBER_NOT_FOUND);
  }

  @Test
  public void 회원탈퇴성공() {
    // given
    doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

    // when
    target.unregist(member.getId());

    // then
  }

  @Test
  public void 프로필수정실패_없는유저() {
    // given
    MemberUpdateRequest updateDto = MemberUpdateRequest.builder().password("test password").build();

    // when
    MemberException exception = assertThrows(MemberException.class, () -> target.updateMember(
        member.getId(), updateDto));

    // then
    assertThat(exception.getErrorResult()).isEqualTo(MemberErrorResult.MEMBER_NOT_FOUND);
  }

  @Test public void 프로필수정_성공() throws Exception {
    // given
    doReturn(Optional.of(member)).when(memberRepository).findById(memberId);
    MemberUpdateRequest updateDto = MemberUpdateRequest.builder().description("description").password("test password").build();

    // when
    MemberResponse result = target.updateMember(memberId, updateDto);
    System.out.println("member: " + result);

    // then
    assertThat(result.getDescription()).isEqualTo("description");
  }

}

