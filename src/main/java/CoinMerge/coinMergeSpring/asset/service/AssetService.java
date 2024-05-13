package CoinMerge.coinMergeSpring.asset.service;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.domain.repository.AssetRepository;
import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.entity.ExchangeConnection;
import CoinMerge.coinMergeSpring.member.domain.entity.Member;
import CoinMerge.coinMergeSpring.member.domain.repository.MemberRepository;
import CoinMerge.coinMergeSpring.member.exception.MemberErrorResult;
import CoinMerge.coinMergeSpring.member.exception.MemberException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {
  @Autowired
  BithumbAssetLoadService bithumbAssetLoadService;
  @Autowired
  BinanceAssetLoadService binanceAssetLoadService;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  AssetRepository assetRepository;

  public List<Asset> updateAssets(String memberId)
      throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    Optional<Member> memberQuery = memberRepository.findById(memberId);
    Member member = memberQuery.orElseThrow(() -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));

    List<ExchangeConnection> exchangeConnections = member.getExchangeConnectionList();
    List<Asset> results = new ArrayList<>();

    for (ExchangeConnection exchangeConnection : exchangeConnections) {
      Exchange exchange = exchangeConnection.getExchange();
      List<Asset> assets;
      if(exchange.getNameKor() == "바이낸스") {
         assets = binanceAssetLoadService.request(memberId, exchange.getId(), exchangeConnection.getAccess_key(), exchangeConnection.getSecret_key());
      }else {
        assets = bithumbAssetLoadService.request(memberId, exchange.getId(), exchangeConnection.getAccess_key(), exchangeConnection.getSecret_key());
      }

      assetRepository.deleteAll(member.getAssetList());
      assetRepository.saveAll(assets);
      results = Stream.of(results, assets).flatMap(asset -> asset.stream()).collect(Collectors.toList()).stream().toList();
    }

    return results;
  }

  public List<Asset> getAssets(String memberId) {
    Optional<Member> memberQuery = memberRepository.findById(memberId);
    Member member = memberQuery.orElseThrow(() -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));

    return member.getAssetList();
  }


}
