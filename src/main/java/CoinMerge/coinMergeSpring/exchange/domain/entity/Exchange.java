package CoinMerge.coinMergeSpring.exchange.domain.entity;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "exchange_id")
  private long id;

  @Column(name="name_kor")
  private String nameKor;

  @Column(name="name_eng")
  private String nameEng;
  private String logo;

  @OneToMany(mappedBy = "exchange", cascade = CascadeType.REMOVE)
  private List<ExchangeConnection> exchangeConnectionList;

  @OneToMany(mappedBy = "exchange", cascade = CascadeType.REMOVE)
  private List<Asset> assetList;

  @Override
  public String toString() {
    return "Exchange{" +
        "id=" + id +
        ", nameKor='" + nameKor + '\'' +
        ", nameEng='" + nameEng + '\'' +
        ", logo='" + logo + '\'' +
        '}';
  }

  public Exchange(long id) {
    this.id = id;
  }
}
