package CoinMerge.coinMergeSpring.asset.service;

import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSignatureHmacSha256;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.dto.BinanceAssetDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BinanceAssetLoadService implements AssetLoadService {

  private static String url = "https://api.binance.com/sapi/v3/asset/getUserAsset";
  @Autowired
  AssetRepository assetRepository;

  @Override
  public URI generateUri(String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("timestamp", String.valueOf(System.currentTimeMillis()));
    map.add("signature",
        generateSignatureHmacSha256("timestamp=" + System.currentTimeMillis(), privateKey));

    return UriComponentsBuilder.fromUriString(url).queryParams(map).build().toUri();
  }

  @Override
  public List<Asset> request(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException {

    WebClient webClient = WebClient.builder().build();
    URI uri = generateUri(accessKey, privateKey);

    BinanceAssetDto[] response = webClient.post()
        .uri(uri)
        .header("X-MBX-APIKEY", accessKey)
        .retrieve()
        .bodyToMono(BinanceAssetDto[].class)
        .doOnError(err -> System.out.println(err.getMessage()))
        .block();

    // asset 처리
    List<Asset> assets = Arrays.stream(response)
        .map(res -> Asset.toAssetFromBinanceAssetDto(memberId, exchangeId, res))
        .toList();

    return assets;
  }

  @Override
  public void load(Asset asset) {
    assetRepository.save(asset);
  }
}
