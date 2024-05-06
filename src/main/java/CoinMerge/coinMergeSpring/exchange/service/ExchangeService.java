package CoinMerge.coinMergeSpring.exchange.service;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.domain.repository.ExchangeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
  @Autowired
  ExchangeRepository exchangeRepository;

  public List<Exchange> getExchanges() {
    return exchangeRepository.findAll();
  }
}
