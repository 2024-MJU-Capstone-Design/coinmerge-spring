package CoinMerge.coinMergeSpring.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import CoinMerge.coinMergeSpring.common.advice.GlobalExceptionHandler;
import CoinMerge.coinMergeSpring.member.controller.MemberController;
import CoinMerge.coinMergeSpring.member.dto.MemberDto;
import CoinMerge.coinMergeSpring.member.dto.MemberResponse;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import CoinMerge.coinMergeSpring.member.service.MemberService;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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
        .build();
  }

  @Test
  @DisplayName("mockMvc Null 체크")
  public void checkMockMvc() {
    assertThat(target).isNotNull();
    assertThat(mockMvc).isNotNull();
  }

  @Test
  @DisplayName("비밀번호를 보내지 않음")
  public void requestSignUpWithoutPassword() throws Exception {
    final String url = "/member/regist";

    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(memberRequest("test@test.com", null, "andy")))
            .contentType(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("이메일을 보내지 않음")
  public void requestSignUpWithoutEmail() throws Exception {
    final String url = "/member/regist";

    final ResultActions resultAction = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(memberRequest(null, "password", "andy")))
            .contentType(MediaType.APPLICATION_JSON)
    );

    resultAction.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Member 등록 중 MerberException 발생")
  public void failWithMemberException() throws Exception {
    // given
    final String url = "/member/regist";
    MemberDto testDto = memberRequest("test@test.com", "Testtest1!", "andy");

    when(memberService.signUp(any(MemberDto.class))).thenThrow(new MemberException(MemberErrorResult.DUPLICATE_MEMBER_REGISTER));

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
  @DisplayName("회원가입 성공")
  public void successSignUp() throws Exception {
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

  private MemberDto memberRequest(final String email, final String password, final String nickname) {
    return MemberDto.builder()
        .email(email)
        .nickname(nickname)
        .password(password)
        .build();
  }
};
