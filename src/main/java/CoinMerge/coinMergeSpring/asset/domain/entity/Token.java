package CoinMerge.coinMergeSpring.asset.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@AllArgsConstructor
@NoArgsConstructor
public class Token {
  @Id
  @Column(name = "token_id")
  private String id;

  @Column(name = "token_name")
  private String name;

  @Column(name = "token_logo")
  private String logo;

  @OneToMany(mappedBy = "token", cascade = CascadeType.REMOVE)
  private List<Asset> assetList;

  public Token(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Token{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", logo='" + logo + '\'' +
        ", assetList=" + assetList +
        '}';
  }
}
