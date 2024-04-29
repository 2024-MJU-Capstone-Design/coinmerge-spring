package CoinMerge.coinMergeSpring.exchange;


import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.exchange.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class ExchangeRepositoryTest {
  @Autowired
  private ExchangeRepository exchangeRepository;

  @Test
  public void 거래소_생성() {
    // given
    Exchange testExchange = Exchange.builder().nameKor("바이낸스").nameEng("binance").logo("test url").build();

    // when
    Exchange exchange = exchangeRepository.save(testExchange);

    // then
    assertThat(exchange).isNotNull();
    assertThat(exchange.getId()).isEqualTo(1);
    assertThat(exchange.getNameKor()).isEqualTo("바이낸스");
    assertThat(exchange.getNameEng()).isEqualTo("binance");
  }
}
