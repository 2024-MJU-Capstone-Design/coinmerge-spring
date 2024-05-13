package CoinMerge.coinMergeSpring.asset.controller;

import CoinMerge.coinMergeSpring.asset.domain.entity.Asset;
import CoinMerge.coinMergeSpring.asset.service.AssetService;
import CoinMerge.coinMergeSpring.common.annotation.LoginRequired;
import CoinMerge.coinMergeSpring.common.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AssetController {
  @Autowired
  AssetService assetService;

  @LoginRequired
  @GetMapping("assets")
  public ResponseEntity<List<Asset>> getAssets(HttpServletRequest httpServletRequest) {
    HttpSession session = httpServletRequest.getSession();
    String memberId = SessionUtil.getMemberId(session);
    return ResponseEntity.ok(assetService.getAssets(memberId));
  }

  @PostMapping("assets")
  public ResponseEntity<List<Asset>> updateAssets(HttpServletRequest httpServletRequest)
      throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    HttpSession session = httpServletRequest.getSession();
    String memberId = SessionUtil.getMemberId(session);
    return ResponseEntity.ok(assetService.updateAssets(memberId));
  }
}
