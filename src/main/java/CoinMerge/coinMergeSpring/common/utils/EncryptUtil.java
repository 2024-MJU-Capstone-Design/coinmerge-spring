package CoinMerge.coinMergeSpring.common.utils;

import static CoinMerge.coinMergeSpring.common.utils.ParserUtil.bytesToHex;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtil {

  public static String generateSHA256HashWithSalt(String message, String salt) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.update(salt.getBytes("UTF-8"));
    byte[] hash = digest.digest(message.getBytes("UTF-8"));
    return bytesToHex(hash);
  }

  public static String generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[16];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().encodeToString(bytes);
  }
}
