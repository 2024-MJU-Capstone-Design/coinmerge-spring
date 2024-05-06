package CoinMerge.coinMergeSpring.asset;

import CoinMerge.coinMergeSpring.asset.service.BinanceAssetLoadService;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BinanceAssetLoadServiceTest {

  @Autowired
  BinanceAssetLoadService binanceAssetLoadService;

  @Test
  public void 바이낸스_자산불러오기_성공() throws NoSuchAlgorithmException, InvalidKeyException {
    binanceAssetLoadService.request(
        "userId",
        1,
        "Z7tQEzHql23BiYeTn3XH4sHvhXaYGV58nM7P84VpsH9Qxd1fsA3gtSxHzi6CWOue",
        "GpjJPsD72QqowNbjWUVegnQMCT7uoVte5SV7MvDNCbxGqHhbPbSu0uF3U7dUid2B");
  }
}
