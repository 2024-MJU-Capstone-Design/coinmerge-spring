package CoinMerge.coinMergeSpring.common.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

  public static final String SESSION_MEMBER_ID = "MEMBER_ID";

  public static String getMemberId(HttpSession session) {
    return (String) session.getAttribute(SESSION_MEMBER_ID);
  }

  public static void logout(HttpSession session) {
    session.removeAttribute(SESSION_MEMBER_ID);
  }
}
