package CoinMerge.coinMergeSpring.exchange;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import CoinMerge.coinMergeSpring.common.LoginInterceptor;
import CoinMerge.coinMergeSpring.common.advice.GlobalExceptionHandler;
import CoinMerge.coinMergeSpring.exchange.controller.ExchangeController;
import CoinMerge.coinMergeSpring.exchange.service.ExchangeService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ExchangeControllerTest {
  @InjectMocks
  private ExchangeController target;
  @Mock
  private ExchangeService exchangeService;
  private Gson gson;
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
    mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new GlobalExceptionHandler())
        .addInterceptors(new LoginInterceptor())
        .build();
  }

  @Test
  public void 모든거래소_받기_성공() throws Exception {
    // given
    final String url = "/exchanges";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url)
    );

    // then
    resultActions.andExpect(status().isOk());
  }
}
