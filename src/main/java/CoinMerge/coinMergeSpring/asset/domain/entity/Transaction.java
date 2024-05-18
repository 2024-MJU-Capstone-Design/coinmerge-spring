package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.BinanceTransactionDto;
import CoinMerge.coinMergeSpring.asset.dto.BinanceWithdrawDto;
import jakarta.persistence.Entity;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private long id;

    private long userid;
    private long exchangeId;
    private int status;
    private String fromTokenId;
    private String toTokenId;
    private int fee;
    private int fromAmount;
    private int fromDollarPrice;
    private int toAmount;
    private int toDollarAmount;
    private String timeStamp;

    public static Transaction toHistoryFromBinanceTransactionDto(BinanceTransactionDto BinanceTransactionDto) {

        return Transaction.builder().build();
    }
}
