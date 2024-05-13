package CoinMerge.coinMergeSpring.exchange;


import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class ExchangeRepositoryTest {
  @Autowired
  private ExchangeRepository exchangeRepository;

  @BeforeEach
  public void setUp() {
    Exchange binance = Exchange.builder().nameKor("바이낸스").nameEng("binance").logo("test url").build();
    Exchange bithumb = Exchange.builder().nameKor("빗썸").nameEng("bithumb").logo("test url").build();

    exchangeRepository.save(binance);
    exchangeRepository.save(bithumb);
  }

  @Test
  public void 거래소_생성() {
    // given
    Exchange testExchange = Exchange.builder().nameKor("바이낸스").nameEng("binance").logo("test url").build();

    // when
    Exchange exchange = exchangeRepository.save(testExchange);

    // then
    assertThat(exchange).isNotNull();
    assertThat(exchange.getNameKor()).isEqualTo("바이낸스");
    assertThat(exchange.getNameEng()).isEqualTo("binance");
  }

  @Test
  public void 거래소_불러오기() {
    // given
    List<Exchange> exchanges = exchangeRepository.findAll();

    // when

    // then
    assertThat(exchanges.size()).isEqualTo(2);
    assertThat(exchanges.get(0).getNameEng()).isEqualTo("binance");
    assertThat(exchanges.get(1).getNameEng()).isEqualTo("bithumb");
  }

  @Test
  public void 거래소_삭제() {
    // given
    Exchange testExchange = Exchange.builder().nameKor("거래소").nameEng("exchange").logo("nothing").build();

    // when
    exchangeRepository.save(testExchange);
    exchangeRepository.deleteById(testExchange.getId());

    // then
    Optional<Exchange> findMyTestExchange = exchangeRepository.findById(testExchange.getId());
    assertThat(findMyTestExchange).isEmpty();
  }
}
