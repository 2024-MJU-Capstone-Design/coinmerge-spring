package CoinMerge.coinMergeSpring.exchange;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.entity.ExchangeConnection;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeConnectionRepository;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeRepository;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class ExchangeConnectionRepositoryTest {

  @Autowired
  private ExchangeConnectionRepository exchangeConnectionRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ExchangeRepository exchangeRepository;

  @Test
  public void 거래소연동_생성() {
    // given
    Member member = memberRepository.save(
        Member.builder().id("test").email("test@test.com").password("password").salt("salt")
            .build());
    Exchange exchange = exchangeRepository.saveAndFlush(Exchange.builder().nameEng("binance").nameKor("바이낸스").build());
    ExchangeConnection testExchangeConnection = ExchangeConnection.builder()
        .access_key("test access key").secret_key("test secret key").member(member)
        .exchange(exchange).build();

    // when
    ExchangeConnection result = exchangeConnectionRepository.save(testExchangeConnection);

    // then
    assertThat(result.getMember().getId()).isEqualTo(member.getId());
    assertThat(result.getExchange().getNameEng()).isEqualTo("binance");
    assertThat(result.getAccess_key()).isEqualTo("test access key");
  }
}
