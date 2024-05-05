package CoinMerge.coinMergeSpring.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import CoinMerge.coinMergeSpring.common.LoginInterceptor;
import CoinMerge.coinMergeSpring.common.advice.GlobalExceptionHandler;
import CoinMerge.coinMergeSpring.member.controller.MemberController;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.dto.MemberUpdateRequest;
import CoinMerge.coinMergeSpring.member.exception.DuplicateNicknameException;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.service.MemberService;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

  @InjectMocks
  private MemberController target;
  private MockMvc mockMvc;
  private Gson gson;
  @Mock
  private MemberService memberService;

  @BeforeEach
  public void beforeEach() {
    gson = new Gson();
    mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new GlobalExceptionHandler())
        .addInterceptors(new LoginInterceptor())
        .build();
  }

  @Test
  public void 회원가입실패_데이터부족() throws Exception {
    final String url = "/member/regist";

    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(memberRequest("test@test.com", null, "andy")))
            .contentType(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void 멤버예외발생() throws Exception {
    // given
    final String url = "/member";
    MemberDto testDto = memberRequest("test@test.com", "Testtest1!", "andy");

    when(memberService.signUp(any(MemberDto.class))).thenThrow(
        new MemberException(MemberErrorResult.DUPLICATE_MEMBER_REGISTER));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(testDto))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void 회원가입_성공() throws Exception {
    // given
    final String url = "/member";
    final MemberResponse memberResponse = MemberResponse.builder().id("test-id")
        .email("test@test.com").build();
    doReturn(memberResponse).when(memberService).signUp(any(MemberDto.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(
                memberRequest("test@test.com", "Testtest1!", "andy")))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isCreated());
    final MemberResponse result = gson.fromJson(
        resultActions.andReturn().getResponse().getContentAsString(
            StandardCharsets.UTF_8), MemberResponse.class);

    assertThat(result.getId()).isEqualTo("test-id");
    assertThat(result.getEmail()).isEqualTo("test@test.com");
  }

  @Test
  public void 닉네임체크_실패() throws Exception {
    // given
    final String url = "/check/nickname";
    when(memberService.checkNickname("앤디")).thenThrow(
        new DuplicateNicknameException(MemberErrorResult.DUPLICATE_NICKNAME));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url)
            .param("nickname", "앤디")
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void 닉네임체크_성공() throws Exception {
    // given
    final String url = "/check/nickname";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url)
            .param("nickname", "앤디")
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  public void 회원탈퇴_세션없음() throws Exception {
    // given
    final String url = "/member";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url)
    );

    // then
    resultActions.andExpect(status().isUnauthorized());
  }

  @Test
  public void 회원탈퇴_성공() throws Exception {
    // given
    String url = "/member";
    MockHttpSession mockSession = new MockHttpSession();
    mockSession.setAttribute("MEMBER_ID", "test");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url).session(mockSession)
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  public void 프로필수정_잘못된정보() throws Exception {
    // given
    final String url = "/member";
    final MemberUpdateRequest updateRequest = MemberUpdateRequest.builder().password("english!23")
        .build();
    MockHttpSession mockSession = new MockHttpSession();
    mockSession.setAttribute("MEMBER_ID", "test");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url).session(mockSession).content(gson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void 프로필수정_세션없음() throws Exception {
    // given
    final String url = "/member";
    final MemberUpdateRequest updateRequest = MemberUpdateRequest.builder().password("english!23")
        .build();

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url).content(gson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isUnauthorized());
  }

  @Test
  public void 프로필수정_성공() throws Exception {
    // given
    final String url = "/member";
    final MemberUpdateRequest updateRequest = MemberUpdateRequest.builder().password("English!23")
        .build();
    MockHttpSession mockSession = new MockHttpSession();
    mockSession.setAttribute("MEMBER_ID", "test");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url).content(gson.toJson(updateRequest))
            .session(mockSession)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  private MemberDto memberRequest(final String email, final String password,
      final String nickname) {
    return MemberDto.builder()
        .email(email)
        .nickname(nickname)
        .password(password)
        .build();
  }
};
