package CoinMerge.coinMergeSpring.common;

import CoinMerge.coinMergeSpring.common.annotation.LoginRequired;
import CoinMerge.coinMergeSpring.common.utils.SessionUtil;
import CoinMerge.coinMergeSpring.member.exception.UnathorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(
        LoginRequired.class)) {
      HttpSession session = request.getSession();
      String memberId = SessionUtil.getMemberId(session);

      if (memberId == null) {
        throw new UnathorizedException();
      }
    }

    return true;
  }
}
