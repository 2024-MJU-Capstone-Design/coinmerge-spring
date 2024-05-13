package CoinMerge.coinMergeSpring.asset;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.service.BinanceAssetLoadService;
import CoinMerge.coinMergeSpring.asset.service.BithumbAssetLoadService;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetLoadServiceTest {

  @Autowired
  BinanceAssetLoadService binanceAssetLoadService;
  @Autowired
  BithumbAssetLoadService bithumbAssetLoadService;

  @Test
  public void 바이낸스_자산불러오기_성공() throws NoSuchAlgorithmException, InvalidKeyException {
    List<Asset> assets = binanceAssetLoadService.request(
        "userId",
        1,
        "Z7tQEzHql23BiYeTn3XH4sHvhXaYGV58nM7P84VpsH9Qxd1fsA3gtSxHzi6CWOue",
        "GpjJPsD72QqowNbjWUVegnQMCT7uoVte5SV7MvDNCbxGqHhbPbSu0uF3U7dUid2B");

    for (Asset asset : assets) {
      System.out.println(asset);
    }
  }

  @Test
  public void 바이낸스_투자내역불러오기_성공() {

  }

  @Test
  public void 빗썸_자산불러오기_성공()
      throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    List<Asset> assets = bithumbAssetLoadService.request(
        "userId",
        1,
        "49625292106557bf699dfd08ecb82267",
        "fe945e5b654ddfa69fdfd64219f5ed1b"
    );

    for (Asset asset : assets) {
      System.out.println(asset);
    }
  }
}
