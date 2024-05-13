package CoinMerge.coinMergeSpring.asset.service;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.Token;
import CoinMerge.coinMergeSpring.common.utils.EncryptUtil;
import CoinMerge.coinMergeSpring.common.utils.ParserUtil;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BithumbAssetLoadService implements AssetLoadService {

  private static String url = "https://api.bithumb.com/info/balance";

  @Override
  public URI generateUri(String accessKey, String privateKey) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    return UriComponentsBuilder.fromUriString(url).queryParams(map).build()
        .encode(StandardCharsets.UTF_8).toUri();
  }

  @Override
  public List<Asset> request(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    URI uri = generateUri(accessKey, privateKey);
    WebClient webClient = WebClient.builder().build();
    String timestamp = String.valueOf(System.currentTimeMillis());
    String encryptData = ParserUtil.encodeStringToUTF8(
        uri.getPath() + ";" + ParserUtil.encodeURIComponent("currency=ALL") + ";" + timestamp);
    String signature = EncryptUtil.generateSignatureHmacSha512(encryptData, privateKey);
    String signatureBase64 = Base64.getEncoder().encodeToString(signature.toLowerCase().getBytes());

    Map<String, LinkedHashMap> response = webClient.post().uri(uri)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).header("Api-Key", accessKey)
        .header("Api-Nonce", timestamp).header("Api-Sign", signatureBase64)
        .header("api-client-type", "2")
        .body(BodyInserters.fromFormData("currency", "ALL")).retrieve().bodyToMono(Map.class)
        .block();

    LinkedHashMap<String, String> data = response.get("data");

    return parseAssetResponse(memberId, exchangeId, data);
  }

  private List<Asset> parseAssetResponse(String memberId, long exchangeId,
      Map<String, String> response) {
    List<Asset> results = new ArrayList<>();
    for (Map.Entry<String, String> entry : response.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key.contains("total_") && Float.parseFloat(value) > 0) {
        String ticker = key.substring(6);
        Token token = new Token(ticker.toUpperCase());
        Exchange exchange = new Exchange(exchangeId);
        Member member = new Member(memberId);

        results.add(Asset.builder().member(member).token(token).amount(Float.parseFloat(value)).member(member)
            .exchange(exchange).build());
      }
    }
    return results;
  }


  @Override
  public void load(Asset asset) {

  }
}
