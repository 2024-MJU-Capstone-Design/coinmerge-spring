package CoinMerge.coinMergeSpring.asset.dto.binance;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinanceTransactionDto {
    private String quoteId;
    private String orderId;
    private String orderStatus;
    private String fromAsset;
    private String fromAmount;
    private String toAsset;
    private String toAmount;
    private String ratio;
    private String inverseRatio;
    private String createTime;

}
