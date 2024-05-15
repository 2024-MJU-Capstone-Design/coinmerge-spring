package CoinMerge.coinMergeSpring.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BinanceWithdrawDto {
    private String id;
    private String amount;
    private String transactionFee;
    private String coin;
    private String status;
    private String address;
    private String txId;
    private String applyTime;
    private String network;
    private String transferType;
    private String withdrawOrderId;
    private String info;
    private String confirmNo;
    private String walletType;
    private String txKey;
    private String completeTime;
}
