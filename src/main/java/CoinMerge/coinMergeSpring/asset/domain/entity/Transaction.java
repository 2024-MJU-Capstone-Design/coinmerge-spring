package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceTransactionDto;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "exchange_id", referencedColumnName = "exchange_id")
    private Exchange exchange;

    private int status;
    private String fromTokenId;
    private String toTokenId;
    private int fee;
    private String fromAmount;
    private int fromDollarPrice;
    private String toAmount;
    private int toDollarAmount;
    private String timeStamp;

    public static Transaction toHistoryFromBinanceTransactionDto(String memberId, long exchangeId, BinanceTransactionDto binanceTransactionDto) {
        Member member = new Member(memberId);
        Exchange exchange = new Exchange(exchangeId);

        return Transaction.builder()
                .id(binanceTransactionDto.getOrderId())
                .member(member)
                .exchange(exchange)
                .fromTokenId(binanceTransactionDto.getFromAsset())
                .toTokenId(binanceTransactionDto.getToAsset())
                .fromAmount(binanceTransactionDto.getFromAmount())
                .toAmount(binanceTransactionDto.getToAmount())
                .build();
    }
}
