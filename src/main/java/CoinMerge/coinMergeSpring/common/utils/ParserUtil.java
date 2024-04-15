package CoinMerge.coinMergeSpring.common.utils;

public class ParserUtil {
  public static String bytesToHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      builder.append(String.format("%02x", bytes[i]));
    }
    return builder.toString();
  }
}
