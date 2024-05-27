package CoinMerge.coinMergeSpring.asset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.Token;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.service.AssetService;
import CoinMerge.coinMergeSpring.asset.service.BinanceAssetLoadService;
import CoinMerge.coinMergeSpring.asset.service.BithumbAssetLoadService;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.entity.ExchangeConnection;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
  @Mock
  MemberRepository memberRepository;
  @Mock
  AssetRepository assetRepository;
  @InjectMocks
  AssetService assetService;
  @Mock
  BinanceAssetLoadService binanceAssetLoadService;
  @Mock
  BithumbAssetLoadService bithumbAssetLoadService;

  @Test
  public void 자산_업데이트_한거래소만있는유저()
      throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    // given
    Member member = getMember(1);
    System.out.println(member);
    doReturn(Optional.of(member)).when(memberRepository).findById("id");
    doReturn(getAssets(1)).when(binanceAssetLoadService).requestDeposit(anyString(), anyLong(), anyString(), anyString());
    doNothing().when(assetRepository).deleteAll(anyCollection());

    // when
    List<Asset> assets = assetService.updateAssets("id");

    // then
    assertThat(assets.size()).isEqualTo(1);
  }

  @Test
  public void 자산_업데이트_모든거래소있는유저()
      throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    // given
    Member member = getMember(2);
    doReturn(Optional.of(member)).when(memberRepository).findById("id");
    doReturn(getAssets(2)).when(binanceAssetLoadService).requestDeposit(anyString(), anyLong(), anyString(), anyString());
    doReturn(getAssets(2)).when(bithumbAssetLoadService).requestDeposit(anyString(), anyLong(), anyString(), anyString());
    doNothing().when(assetRepository).deleteAll(anyCollection());

    // when
    List<Asset> assets = assetService.updateAssets("id");

    // then
    assertThat(assets.size()).isEqualTo(4);
  }

  @Test
  public void 자산_업데이트_아무거래소없음()
      throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    // given
    Member member = getMember(0);
    System.out.println(member);
    doReturn(Optional.of(member)).when(memberRepository).findById("id");

    // when
    List<Asset> assets = assetService.updateAssets("id");

    // then
    assertThat(assets.size()).isEqualTo(0);
  }

  @Test
  public void 자산_불러오기_성공() {
    // given
    Member member = getMember(2);
    doReturn(Optional.of(member)).when(memberRepository).findById("id");

    // when
    List<Asset> assets = memberRepository.findById("id").get().getAssetList();

    // then
    assertThat(assets.size()).isEqualTo(2);
  }

  public Member getMember(int len) {
    return Member.builder().assetList(getAssets(len)).id("id").password("password")
        .email("email@email.com").exchangeConnectionList(getExchangeConnection(len)).salt("salt").build();
  }

  public List<Asset> getAssets(int len) {
    List<Asset> assets = new ArrayList<>();
    List<ExchangeConnection> exchangeConnections = getExchangeConnection(len);
    List<Token> tokens = getTokens();

    for (int i = 0; i < len; i++) {
      assets.add(Asset.builder().member(exchangeConnections.get(i).getMember())
          .exchange(exchangeConnections.get(i).getExchange()).token(tokens.get(i)).build());
    }

    return assets;
  }

  public List<Token> getTokens() {
    List<Token> tokens = new ArrayList<>();
    tokens.add(Token.builder().id("BTC").name("bitcoin").logo("logo").build());
    tokens.add(Token.builder().id("ETH").name("ethereum").logo("logo").build());
    tokens.add(Token.builder().id("USDC").name("USDC").logo("logo").build());

    return tokens;
  }

  public List<ExchangeConnection> getExchangeConnection(int len) {
    List<Exchange> exchanges = new ArrayList<>();
    exchanges.add(Exchange.builder().id(1).nameKor("바이낸스").nameEng("binance").build());
    exchanges.add(Exchange.builder().id(1).nameKor("빗썸").nameEng("binance").build());

    List<ExchangeConnection> exchangeConnections = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      exchangeConnections.add(
          ExchangeConnection.builder().id(i).exchange(exchanges.get(i)).access_key("acc").secret_key("secret").build());
    }

    return exchangeConnections;
  }

}
