package CoinMerge.coinMergeSpring.asset.dto.binance;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinanceSnapshotDto {
    private String asset;
    private String free;
    private String locked;
    private long timestamp;

}
