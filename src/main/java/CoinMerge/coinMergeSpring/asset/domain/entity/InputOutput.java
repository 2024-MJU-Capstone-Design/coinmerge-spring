package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.BinanceAssetDto;
import CoinMerge.coinMergeSpring.asset.dto.BinanceDepositDto;
import CoinMerge.coinMergeSpring.asset.dto.BinanceWithdrawDto;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InputOutput {
    private long id;

    private long userId;
    private int exchangeId;
    private String tokenId;
    private String timeStamp;
    private String amount;
    private int type;
    private int fee;
    private int dollarPrice;

    public static InputOutput toHistoryFromBinanceDepositDto(BinanceDepositDto binanceDepositDto) {

        return InputOutput.builder().amount(binanceDepositDto.getAmount()).timeStamp(binanceDepositDto.getInsertTime())
                .type(0).build();
    }

    public static InputOutput toHistoryFromBinanceDepositDto(BinanceWithdrawDto binanceWithdrawDto) {
        return InputOutput.builder().amount(binanceWithdrawDto.getAmount()).timeStamp(binanceWithdrawDto.getCompleteTime())
                .type(1).build();
    }
}
