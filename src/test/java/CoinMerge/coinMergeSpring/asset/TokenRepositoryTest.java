package CoinMerge.coinMergeSpring.asset;

import static org.assertj.core.api.Assertions.assertThat;

import CoinMerge.coinMergeSpring.asset.domain.entity.Token;
import CoinMerge.coinMergeSpring.asset.domain.repository.TokenRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.yml")
@DataJpaTest
public class TokenRepositoryTest {
  @Autowired
  TokenRepository tokenRepository;

  @Test
  public void 토큰_저장() {
    // given
    Token token = Token.builder().id("USDC").name("USD Coin").logo("test url").build();

    // when
    Token saveResultToken = tokenRepository.save(token);

    // then
    assertThat(saveResultToken).isNotNull();
    assertThat(saveResultToken.getId()).isEqualTo(token.getId());
  }

  @Test
  public void 토큰_삭제() {
    // given
    Token testToken = Token.builder().id("USDC").name("USD Coin").logo("test url").build();

    // when
    tokenRepository.save(testToken);
    tokenRepository.deleteById(testToken.getId());

    // then
    Optional<Token> findTokenResult = tokenRepository.findById(testToken.getId());
    assertThat(findTokenResult).isEmpty();
  }

  @Test
  public void 토큰_불러오기() {
    // given
    tokenRepository.save(Token.builder().id("USDC").name("USD Coin").logo("test url").build());
    tokenRepository.save(Token.builder().id("BTC").name("Bitcoin").logo("test url").build());

    // when
    List<Token> tokens = tokenRepository.findAll();

    // then
    assertThat(tokens.size()).isEqualTo(2);
    assertThat(tokens.get(0)).isNotNull();
    assertThat(tokens.get(1)).isNotNull();
  }
}
