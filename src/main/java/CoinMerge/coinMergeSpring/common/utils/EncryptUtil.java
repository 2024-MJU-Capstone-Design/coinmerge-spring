package CoinMerge.coinMergeSpring.common.utils;

import org.springframework.data.redis.connection.convert.StringToDataTypeConverter;

import static CoinMerge.coinMergeSpring.common.utils.ParserUtil.bytesToHex;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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

  public static String generateSignatureHmacSha256(String text, String key)
      throws NoSuchAlgorithmException, InvalidKeyException {

    SecretKeySpec hmacKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(hmacKeySpec);
    return bytesToHex(mac.doFinal(text.getBytes()));
  }

  public static String generateSignatureHmacSha512(String text, String key)
      throws NoSuchAlgorithmException, InvalidKeyException {
    SecretKeySpec hmacKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
    Mac mac = Mac.getInstance("HmacSHA512");
    mac.init(hmacKeySpec);
    return bytesToHex(mac.doFinal(text.getBytes()));
  }
}
