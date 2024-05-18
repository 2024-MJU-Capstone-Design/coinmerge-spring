package CoinMerge.coinMergeSpring.asset.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinanceDepositDto {
    private String id;
    private String amount;
    private String coin;
    private String network;
    private String status;
    private String address;
    private String addressTag;
    private String txId;
    private String insertTime;
    private String transferType;
    private String confirmTimes;
    private String unlockConfirm;
    private String walletType;

}
