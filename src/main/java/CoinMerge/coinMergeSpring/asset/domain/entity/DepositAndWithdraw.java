package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceDepositDto;
import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceWithdrawDto;
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
@ToString
public class DepositAndWithdraw {
    @Id
    @Column(name = "withdraw_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "exchange_id", referencedColumnName = "exchange_id")
    private Exchange exchange;

    private String tokenId;
    private String timeStamp;
    private String amount;
    private int type;
    private int fee;
    private int dollarPrice;

    public static DepositAndWithdraw toHistoryFromBinanceDepositDto(String memberId, long exchangeId, BinanceDepositDto binanceDepositDto) {
        Member member = new Member(memberId);
        Exchange exchange = new Exchange(exchangeId);

        return DepositAndWithdraw.builder()
                .member(member)
                .id(binanceDepositDto.getId())
                .exchange(exchange)
                .tokenId(binanceDepositDto.getCoin())
                .timeStamp(binanceDepositDto.getInsertTime())
                .amount(binanceDepositDto.getAmount())
                .type(0)
                .build();
    }

    public static DepositAndWithdraw toHistoryFromBinanceWithdrawDto(String memberId, long exchangeId, BinanceWithdrawDto binanceWithdrawDto) {
        Member member = new Member(memberId);
        Exchange exchange = new Exchange(exchangeId);

        return DepositAndWithdraw.builder()
                .member(member)
                .exchange(exchange)
                .tokenId(binanceWithdrawDto.getCoin())
                .tokenId(binanceWithdrawDto.getCoin())
                .amount(binanceWithdrawDto.getAmount())
                .timeStamp(binanceWithdrawDto.getCompleteTime())
                .type(1).build();
    }
}
