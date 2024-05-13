package CoinMerge.coinMergeSpring.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ParserUtil {
  public static String bytesToHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      builder.append(String.format("%02X", bytes[i]));
    }
    return builder.toString();
  }

  public static String encodeURIComponent(String text) throws UnsupportedEncodingException {
    String result = null;

    try {
      result = URLEncoder.encode(text, "UTF-8")
          .replaceAll("\\+", "%20")
          .replaceAll("\\%21", "!")
          .replaceAll("\\%27", "'")
          .replaceAll("\\%28", "(")
          .replaceAll("\\%29", ")")
          .replaceAll("\\%26", "&")
          .replaceAll("\\%3D", "=")
          .replaceAll("\\%7E", "~");
    }

    // This exception should never occur.
    catch (UnsupportedEncodingException e) {
      result = text;
    }

    return result;
  }

  public static String encodeStringToUTF8(String str) {
    try {
      return new String(str.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Unsupported encoding: UTF-8", e);
    }
  }
}