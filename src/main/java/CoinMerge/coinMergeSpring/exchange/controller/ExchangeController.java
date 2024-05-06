package CoinMerge.coinMergeSpring.exchange.controller;

import CoinMerge.coinMergeSpring.exchange.domain.entity.Exchange;
import CoinMerge.coinMergeSpring.exchange.service.ExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExchangeController {

  @Autowired
  ExchangeService exchangeService;

  @GetMapping("exchanges")
  public ResponseEntity<List<Exchange>> getExchanges(HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok().body(exchangeService.getExchanges());
  }
}
