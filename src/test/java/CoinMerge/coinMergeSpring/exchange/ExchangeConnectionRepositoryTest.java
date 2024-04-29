package CoinMerge.coinMergeSpring.exchange;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.exchange.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.entity.ExchangeConnection;
import CoinMerge.coinMergeSpring.exchange.repository.ExchangeConnectionRepository;
import CoinMerge.coinMergeSpring.exchange.repository.ExchangeRepository;
import CoinMerge.coinMergeSpring.member.MemberRepositoryTest;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class ExchangeConnectionRepositoryTest {

  @Autowired
  private ExchangeConnectionRepository exchangeConnectionRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ExchangeRepository exchangeRepository;

  @BeforeEach
  public void init() {
    System.out.println("called");
    memberRepository.save(Member.builder().id("test").email("test@test.com").password("password").salt("salt").build());
    exchangeRepository.save(Exchange.builder().nameEng("binance").nameKor("바이낸스").build());
  }

  @Test
  public void 거래소연동_생성() {
    // given
    Member testMember = Member.builder().id("test").build();
    Exchange testExchange = Exchange.builder().id(1).build();
    ExchangeConnection testExchangeConnection = ExchangeConnection.builder()
        .access_key("test access key").secret_key("test secret key").member(testMember).exchange(testExchange).build();

    // when
    ExchangeConnection result = exchangeConnectionRepository.save(testExchangeConnection);

    // then
    assertThat(result.getMember().getId()).isEqualTo("test");
    assertThat(result.getExchange().getNameEng()).isEqualTo("binance");
    assertThat(result.getAccess_key()).isEqualTo("test access key");
  }
}
