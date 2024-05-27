package CoinMerge.coinMergeSpring.asset.domain.entity;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceAssetDto;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "asset_id")
  private long id;

  @ManyToOne
  @JoinColumn(name = "token_id", referencedColumnName = "token_id")
  private Token token;

  @ManyToOne
  @JoinColumn(name = "member_id", referencedColumnName = "member_id")
  private Member member;

  private float amount;

  @ManyToOne
  @JoinColumn(name = "exchange_id", referencedColumnName = "exchange_id")
  private Exchange exchange;

  public static Asset toAssetFromBinanceAssetDto(String memberId, long exchangeId, BinanceAssetDto binanceAssetDto) {
    Token token = new Token(binanceAssetDto.getAsset());
    Member member = new Member(memberId);
    float amount = Float.parseFloat(binanceAssetDto.getFree()) + Float.parseFloat(binanceAssetDto.getLocked()) + Float.parseFloat(
        binanceAssetDto.getIpoable());
    Exchange exchange = new Exchange(exchangeId);

    return Asset.builder().token(token).member(member).amount(amount).exchange(exchange).build();
  }

  @Override
  public String toString() {
    return "Asset{" +
        "id=" + id +
        ", token=" + token +
        ", member=" + member +
        ", amount=" + amount +
        ", exchange=" + exchange +
        '}';
  }
}
