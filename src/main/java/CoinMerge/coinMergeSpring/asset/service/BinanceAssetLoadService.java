package CoinMerge.coinMergeSpring.asset.service;

import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSignatureHmacSha256;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.InputOutput;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.dto.BinanceAssetDto;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import CoinMerge.coinMergeSpring.asset.dto.BinanceDepositDto;
import CoinMerge.coinMergeSpring.asset.dto.BinanceWithdrawDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service @Slf4j
public class BinanceAssetLoadService implements AssetLoadService {

  private static String url;
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

    List<Asset> asset =  requestAsset(memberId,exchangeId,accessKey,privateKey);
    requestDeposit(memberId,exchangeId,accessKey,privateKey);
    requestWithdraw(memberId,exchangeId,accessKey,privateKey);

    return asset;
  }


  @Override
  public List<Asset> requestAsset(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException {
    url = "https://api.binance.com/sapi/v3/asset/getUserAsset";
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


  public List<InputOutput> requestDeposit(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    WebClient webClient = WebClient.builder().build();
    url = "https://api.binance.com/sapi/v1/capital/deposit/hisrec";
    URI uri = generateUri(accessKey, privateKey);
    log.info(String.valueOf(uri));
    BinanceDepositDto[] response = webClient.get()
            .uri(uri)
            .header("X-MBX-APIKEY", accessKey)
            .retrieve()
            .bodyToMono(BinanceDepositDto[].class)
            .doOnError(err -> System.out.println(err.getMessage()))
            .block();

    log.info(String.valueOf(response.length));
    // asset 처리
    List<InputOutput> assets = Arrays.stream(response)
            .map(res -> InputOutput.toHistoryFromBinanceDepositDto(res))
            .toList();


    return assets;
  }


  public List<InputOutput> requestWithdraw(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    WebClient webClient = WebClient.builder().build();
    url = "https://api.binance.com/sapi/v1/capital/withdraw/history";
    URI uri = generateUri(accessKey, privateKey);
    log.info(String.valueOf(uri));
    BinanceWithdrawDto[] response = webClient.get()
            .uri(uri)
            .header("X-MBX-APIKEY", accessKey)
            .retrieve()
            .bodyToMono(BinanceWithdrawDto[].class)
            .doOnError(err -> System.out.println(err.getMessage()))
            .block();

    // asset 처리
    List<InputOutput> assets = Arrays.stream(response)
            .map(res -> InputOutput.toHistoryFromBinanceDepositDto(res))
            .toList();


    return assets;
  }

  @Override
  public List<Asset> requestTransaction(String memberId, long exchangeId, String accessKey, String privateKey) {
    return null;
  }

  @Override
  public void load(Asset asset) {
    assetRepository.save(asset);
  }
}
