package CoinMerge.coinMergeSpring.exchange.entity;

import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeConnection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="exchange_connection_id")
  private long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="member_id", referencedColumnName = "member_id")
  private Member member;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="exchange_id", referencedColumnName = "exchange_id")
  private Exchange exchange;

  private String access_key;
  private String secret_key;

  @Override
  public String toString() {
    return "ExchangeConnection{" +
        "id=" + id +
        ", member=" + member +
        ", exchange=" + exchange +
        ", access_key='" + access_key + '\'' +
        ", secret_key='" + secret_key + '\'' +
        '}';
  }
}
