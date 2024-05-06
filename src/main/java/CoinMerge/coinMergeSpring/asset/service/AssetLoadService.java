package CoinMerge.coinMergeSpring.asset.service;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public interface AssetLoadService {

  URI generateUri(String publicKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException;

  Asset[] request(String memberId, long exchangeId, String accessKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeyException;

  void load(Asset asset);
}
