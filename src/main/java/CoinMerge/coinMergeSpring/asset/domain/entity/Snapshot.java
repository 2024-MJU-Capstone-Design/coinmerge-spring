package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceSnapshotDto;
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
public class Snapshot {
    @Id
    @Column(name = "snapshot_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    private String token;
    private String timeStamp;
    private String exchangePrice;
    public static Snapshot toSnapshotFromBinanceSnapshotDto(String memberId, BinanceSnapshotDto binanceSnapshotDto) {
        Member member = new Member(memberId);

        return Snapshot.builder()
                .id(binanceSnapshotDto.getTimestamp() + binanceSnapshotDto.getAsset())
                .member(member)
                .token(binanceSnapshotDto.getAsset())
                .timeStamp(String.valueOf(binanceSnapshotDto.getTimestamp()))
                .exchangePrice(binanceSnapshotDto.getFree())
                .build();
    }
}
