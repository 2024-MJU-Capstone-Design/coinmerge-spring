package CoinMerge.coinMergeSpring.exchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeRepository;
import CoinMerge.coinMergeSpring.exchange.service.ExchangeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {
  @Mock
  ExchangeRepository exchangeRepository;
  @InjectMocks
  ExchangeService exchangeService;
  List<Exchange> exchanges;

  @BeforeEach
  public void setUp() {
    exchanges = List.of(Exchange.builder().nameKor("바이낸스").build(),
        Exchange.builder().nameKor("빗썸").build());
  }

  @Test
  public void 거래소_불러오기_성공() {
    // given
    doReturn(exchanges).when(exchangeRepository).findAll();

    // when
    List<Exchange> allExchanges = exchangeService.getExchanges();

    // then
    assertThat(allExchanges).isNotEmpty();
  }
}
