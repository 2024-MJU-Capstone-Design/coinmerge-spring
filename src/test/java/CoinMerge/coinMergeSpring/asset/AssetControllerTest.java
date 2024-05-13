package CoinMerge.coinMergeSpring.asset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import CoinMerge.coinMergeSpring.asset.controller.AssetController;
import CoinMerge.coinMergeSpring.asset.service.AssetService;
import CoinMerge.coinMergeSpring.common.LoginInterceptor;
import CoinMerge.coinMergeSpring.common.advice.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AssetControllerTest {

  private Gson gson;

  @Mock
  private AssetService assetService;

  @InjectMocks
  private AssetController target;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    gson = new Gson();
    mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new GlobalExceptionHandler())
        .addInterceptors(new LoginInterceptor())
        .build();
  }

  @Test
  public void 자산_불러오기_성공() throws Exception {
    // given
    String url = "/assets";
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("MEMBER_ID", "test");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url)
            .session(session)
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  public void 자산_업데이트_성공() throws Exception {
    // given
    String url = "/assets";
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("MEMBER_ID", "test");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .session(session)
    );

    // then
    resultActions.andExpect(status().isOk());
  }

}
