package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.BinanceDepositDto;
import CoinMerge.coinMergeSpring.asset.dto.BinanceWithdrawDto;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepositAndWithdraw {
    private long id;

    private long userId;
    private int exchangeId;
    private String tokenId;
    private String timeStamp;
    private String amount;
    private int type;
    private int fee;
    private int dollarPrice;

    public static DepositAndWithdraw toHistoryFromBinanceDepositDto(BinanceDepositDto binanceDepositDto) {

        return DepositAndWithdraw.builder().amount(binanceDepositDto.getAmount()).timeStamp(binanceDepositDto.getInsertTime())
                .type(0).build();
    }

    public static DepositAndWithdraw toHistoryFromBinanceWithdrawDto(BinanceWithdrawDto binanceWithdrawDto) {
        return DepositAndWithdraw.builder().amount(binanceWithdrawDto.getAmount()).timeStamp(binanceWithdrawDto.getCompleteTime())
                .type(1).build();
    }
}
