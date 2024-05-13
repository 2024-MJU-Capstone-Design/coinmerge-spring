package CoinMerge.coinMergeSpring.asset;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.Token;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.domain.repository.TokenRepository;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeRepository;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AssetRepositoryTest {
  @Autowired
  AssetRepository assetRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  ExchangeRepository exchangeRepository;
  @Autowired
  TokenRepository tokenRepository;

  @Test
  void 자산_저장_성공() {
    // given
    Asset testAsset = getAsset();

    // when
    Asset result = assetRepository.save(testAsset);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getToken().getId()).isEqualTo("BTC");
    assertThat(result.getAmount()).isEqualTo(1.3E-7F);
  }

  public Asset getAsset() {
    Token token = Token.builder().id("BTC").logo("test logo").name("Bitcoin").build();
    Member member = Member.builder().email("email").password("pass").salt("salt").build();
    Exchange testExchange = new Exchange(1);

    memberRepository.save(member);
    tokenRepository.save(token);
    exchangeRepository.save(testExchange);

    return Asset.builder().token(token).member(member).exchange(testExchange).amount(1.3E-7F).build();
  }
}
