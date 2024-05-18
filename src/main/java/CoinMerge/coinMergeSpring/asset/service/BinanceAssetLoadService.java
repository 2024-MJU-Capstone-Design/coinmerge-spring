package CoinMerge.coinMergeSpring.asset.service;

import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSignatureHmacSha256;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.DepositAndWithdraw;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.domain.repository.DepositWithdrawRepository;
import CoinMerge.coinMergeSpring.asset.dto.*;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service @Slf4j
public class BinanceAssetLoadService implements AssetLoadService {

  @Autowired
  AssetRepository assetRepository;
  @Autowired
  DepositWithdrawRepository depositWithdrawRepository;

  @Override
  public URI generateUri(String accessKey, String privateKey, String url)
          throws NoSuchAlgorithmException, InvalidKeyException {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("timestamp", String.valueOf(System.currentTimeMillis()));
    log.info(String.valueOf(System.currentTimeMillis()));
    map.add("signature",
            generateSignatureHmacSha256("timestamp=" + System.currentTimeMillis(), privateKey));

    return UriComponentsBuilder.fromUriString(url).queryParams(map).build().toUri();
  }


  public URI generateUriStartEndTime(String accessKey, String privateKey, String url, long start, long end)
          throws NoSuchAlgorithmException, InvalidKeyException {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("startTime", String.valueOf(start));
    map.add("endTime", String.valueOf(end));

    map.add("timestamp", String.valueOf(System.currentTimeMillis()));

    map.add("signature",
            generateSignatureHmacSha256("startTime=" + start + "&endTime=" + end +
                    "&timestamp=" + System.currentTimeMillis(), privateKey));

    return UriComponentsBuilder.fromUriString(url).queryParams(map).build().toUri();
  }


  @Override
  public List<Asset> requestAsset(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException {
    String url = "https://api.binance.com/sapi/v3/asset/getUserAsset";
    WebClient webClient = WebClient.builder().build();
    URI uri = generateUri(accessKey, privateKey, url);

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
//7776000000

  public List<DepositAndWithdraw> requestDeposit(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<DepositAndWithdraw> out = new ArrayList<>();
    long ninety = 86400000*89;
    long end = System.currentTimeMillis();
    long start = end - ninety;

    for (int i = 0; i < 5; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/capital/deposit/hisrec";
      //URI uri = generateUriDeposit(accessKey, privateKey, url, start, end);
      URI uri = generateUri(accessKey, privateKey, url);
      log.info(String.valueOf(uri));

      BinanceDepositDto[] response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(BinanceDepositDto[].class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();

      System.out.println(response[0]);
      // asset 처리
      List<DepositAndWithdraw> assets = Arrays.stream(response)
              .map(res -> DepositAndWithdraw.toHistoryFromBinanceDepositDto(res))
              .toList();

      out.addAll(assets);
      end = start - 86400000;
      start = end - ninety;
    }
    saveDepositWithdraw(out);

    return out;
  }


  public List<DepositAndWithdraw> requestWithdraw(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<DepositAndWithdraw> out = new ArrayList<>();
    long ninety = 86400000*89;
    long end = System.currentTimeMillis();
    long start = end - ninety;

    for (int i = 0; i < 5; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/capital/withdraw/history";
      URI uri = generateUri(accessKey, privateKey, url);
      log.info(String.valueOf(uri));
      BinanceWithdrawDto[] response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(BinanceWithdrawDto[].class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();

      // asset 처리
      List<DepositAndWithdraw> assets = Arrays.stream(response)
              .map(res -> DepositAndWithdraw.toHistoryFromBinanceWithdrawDto(res))
              .toList();
      out.addAll(assets);
      end = start - 86400000;
      start = end - ninety;

    }
    saveDepositWithdraw(out);
    return out;
  }

  private void saveDepositWithdraw(List<DepositAndWithdraw> out) {
    for (DepositAndWithdraw depositAndWithdraw : out) {
      if(depositWithdrawRepository.exists(depositAndWithdraw)){
        depositWithdrawRepository.save(depositAndWithdraw);
      }
    }
  }

  @Override
  public List<BinanceTransactionDto> requestTransaction(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<BinanceTransactionDto> out = null;
    long ninety =  86400000 * 30;
    long end = System.currentTimeMillis();
    long start = end - ninety;

    for (int i = 0; i < 24; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/convert/tradeFlow";
      URI uri = generateUriStartEndTime(accessKey, privateKey, url, start, end);
      log.info(String.valueOf(uri));
      Map<String, LinkedHashMap> response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(Map.class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();

      //List<String, String> data = response.get("list");
      System.out.println(response);
      System.out.println(response.get("list"));


      end = start - 86400000;
      start = end - ninety;
    }


    return out;
  }

  @Override
  public void load(Asset asset) {
    assetRepository.save(asset);
  }
}
