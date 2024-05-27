package CoinMerge.coinMergeSpring.asset.service;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceTransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface AssetLoadService {

  URI generateUri(String publicKey, String privateKey, String url)
      throws NoSuchAlgorithmException, InvalidKeyException;

  List<Asset> requestAsset(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

  //입금

  //출금

  void load(Asset asset);
}
