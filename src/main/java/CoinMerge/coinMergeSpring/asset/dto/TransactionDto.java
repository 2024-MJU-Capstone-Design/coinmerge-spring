package CoinMerge.coinMergeSpring.asset.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private List<BinanceTransactionDto> list;
    private String startTime;
    private String endTime;
    private String limit;
    private String moreData;

}
