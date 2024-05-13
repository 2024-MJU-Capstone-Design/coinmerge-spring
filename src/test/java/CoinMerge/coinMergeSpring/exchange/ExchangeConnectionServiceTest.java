package CoinMerge.coinMergeSpring.exchange;

import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeConnectionRepository;
import CoinMerge.coinMergeSpring.exchange.service.ExchangeConnectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExchangeConnectionServiceTest {
  @Mock
  private ExchangeConnectionRepository exchangeConnectionRepository;

  @InjectMocks
  private ExchangeConnectionService exchangeConnectionService;

  @Test
  public void 거래소연동실패_유효하지않은엑세스키() {

  }

  @Test
  public void 거래소연동실패_유효하지않은프라이빗키() {

  }
}
