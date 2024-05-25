package CoinMerge.coinMergeSpring.asset.service;

import static CoinMerge.coinMergeSpring.asset.domain.entity.Transaction.toHistoryFromBinanceTransactionDto;
import static CoinMerge.coinMergeSpring.common.utils.EncryptUtil.generateSignatureHmacSha256;
import static CoinMerge.coinMergeSpring.common.utils.ParserUtil.*;


import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.entity.DepositAndWithdraw;
import CoinMerge.coinMergeSpring.asset.domain.entity.Snapshot;
import CoinMerge.coinMergeSpring.asset.domain.entity.Transaction;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.asset.domain.repository.DepositWithdrawRepository;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import CoinMerge.coinMergeSpring.asset.domain.repository.SnapshotRepository;
import CoinMerge.coinMergeSpring.asset.domain.repository.TransactionRepository;
import CoinMerge.coinMergeSpring.asset.dto.binance.*;
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
  @Autowired
  TransactionRepository transactionRepository;
  @Autowired
  SnapshotRepository snapshotRepository;

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
    //System.out.println(start + " " + end + " " + map.get("timestamp") + " " + map.get("signature"));
    return UriComponentsBuilder.fromUriString(url).queryParams(map).build().toUri();
  }

  public URI generateUriType(String accessKey, String privateKey, String url, long start, long end, String type)
          throws NoSuchAlgorithmException, InvalidKeyException {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("type", type);
    map.add("startTime", String.valueOf(start));
    map.add("endTime", String.valueOf(end));

    map.add("timestamp", String.valueOf(System.currentTimeMillis()));
    map.add("signature",
            generateSignatureHmacSha256("type=" + type + "&startTime=" + start + "&endTime=" + end +
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

    //requestDeposit(memberId,exchangeId,accessKey,privateKey);
    //requestWithdraw(memberId,exchangeId,accessKey,privateKey);
    return assets;
  }
//7776000000

  public List<DepositAndWithdraw> requestDeposit(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<DepositAndWithdraw> out = new ArrayList<>();
    long minus = 86400000L * 80;
    long end = System.currentTimeMillis();
    long start = end - minus;

    for (int i = 0; i < 10; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/capital/deposit/hisrec";
      URI uri = generateUriStartEndTime(accessKey, privateKey, url, start, end);

      BinanceDepositDto[] response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(BinanceDepositDto[].class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();


      // asset 처리
      List<DepositAndWithdraw> assets = Arrays.stream(response)
              .map(res -> DepositAndWithdraw.toHistoryFromBinanceDepositDto(memberId, exchangeId, res))
              .toList();
      for (DepositAndWithdraw asset : assets) {
        System.out.println(asset);
      }
      out.addAll(assets);
      end = start - 1;
      start = end - minus;
    }
    saveDepositWithdraw(out);

    return out;
  }


  public List<DepositAndWithdraw> requestWithdraw(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<DepositAndWithdraw> out = new ArrayList<>();
    long minus = 86400000L * 80;
    long end = System.currentTimeMillis();
    long start = end - minus;

    for (int i = 0; i < 10; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/capital/withdraw/history";
      URI uri = generateUriStartEndTime(accessKey, privateKey, url, start, end);

      BinanceWithdrawDto[] response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(BinanceWithdrawDto[].class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();

      // asset 처리
      List<DepositAndWithdraw> assets = Arrays.stream(response)
              .map(res -> DepositAndWithdraw.toHistoryFromBinanceWithdrawDto(memberId,exchangeId, res))
              .toList();
      for (DepositAndWithdraw asset : assets) {
        System.out.println(asset);
      }
      out.addAll(assets);
      end = start - 1;
      start = end - minus;

    }
    saveDepositWithdraw(out);
    return out;
  }



  public List<Transaction> requestTransaction(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<Transaction> out = new ArrayList<>();
    long minus =  86400000L * 20;
    long end = System.currentTimeMillis();
    long start = end - minus;

    for (int i = 0; i < 25; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/convert/tradeFlow";
      URI uri = generateUriStartEndTime(accessKey, privateKey, url, start, end);
      Map<String, LinkedHashMap> response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(Map.class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();

      for (BinanceTransactionDto tradeFlow : binanceTransactionHistoryParser(response)) {
        out.add(toHistoryFromBinanceTransactionDto(memberId, exchangeId, tradeFlow));
      }

      end = start - 1;
      start = end - minus;
    }

    for (Transaction transaction : out) {
      System.out.println(transaction);
    }

    saveTransaction(out);
    return out;
  }



  public List<Snapshot> requestSnapshot(String memberId, long exchangeId, String accessKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
    List<Snapshot> out = new ArrayList<>();
    long minus = 86400000L * 30;
    long end = System.currentTimeMillis();
    long start = end - minus;
    long timestamp = 0L;
    for (int i = 0; i < 5; i++) {
      WebClient webClient = WebClient.builder().build();
      String url = "https://api.binance.com/sapi/v1/accountSnapshot";
      URI uri = generateUriType(accessKey, privateKey, url, start, end, "SPOT");

      Map<String, LinkedHashMap> response = webClient.get()
              .uri(uri)
              .header("X-MBX-APIKEY", accessKey)
              .retrieve()
              .bodyToMono(Map.class)
              .doOnError(err -> System.out.println(err.getMessage()))
              .block();


      System.out.println(binanceSnapshotParser(response));
      //LinkedHashMap arr = response.get("snapshotVos");
      //System.out.println(arr.get("data"));
      for (BinanceSnapshotDto s : snapshotParser(timestamp, response)) {
        out.add(Snapshot.toSnapshotFromBinanceSnapshotDto(memberId, s));
      }


      end = start - 1;
      start = end - minus;
    }

    saveSnapshot(out);

    return out;
  }



  private void saveDepositWithdraw(List<DepositAndWithdraw> out) {
    for (DepositAndWithdraw depositAndWithdraw : out) {
      if(depositWithdrawRepository.existsById(depositAndWithdraw.getId())){
        depositWithdrawRepository.save(depositAndWithdraw);
      }
    }
  }

  private void saveTransaction(List<Transaction> out) {
    for (Transaction transaction : out) {
      if(transactionRepository.existsById(transaction.getId())){
        transactionRepository.save(transaction);
      }
    }
  }

  private void saveSnapshot(List<Snapshot> out) {
    for (Snapshot snapshot : out) {
      if(snapshotRepository.existsById(snapshot.getId())){
        snapshotRepository.save(snapshot);
      }
    }
  }

  @Override
  public void load(Asset asset) {
    assetRepository.save(asset);
  }
}
