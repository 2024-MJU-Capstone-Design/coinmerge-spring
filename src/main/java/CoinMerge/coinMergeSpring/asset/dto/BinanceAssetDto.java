package CoinMerge.coinMergeSpring.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BinanceAssetDto {
  private String asset;
  private String free;
  private String locked;
  private String withdrawing;
  private String ipoable;
  private String btcValuation;
}
