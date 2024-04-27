package CoinMerge.coinMergeSpring.interlock;

import org.apache.tomcat.util.buf.HexUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
public class util {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String base64Encode(byte[] bytes) {

		Base64.Encoder encoder = Base64.getEncoder();

		String bytesEncoded = new String(encoder.encode(bytes));
		return bytesEncoded;
    }

	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0, v; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

    public static String hashToString(String data, byte[] key) {
		String result = null;
		Mac sha512_HMAC;
	
		try {
		    sha512_HMAC = Mac.getInstance("HmacSHA512");
		    System.out.println("key : " + new String(key));
		    SecretKeySpec secretkey = new SecretKeySpec(key, "HmacSHA512");
		    sha512_HMAC.init(secretkey);
	
		    byte[] mac_data = sha512_HMAC.doFinal(data.getBytes());
		    System.out.println("hex : " + bin2hex(mac_data));

			Base64.Encoder encoder = Base64.getEncoder();
			result = String.valueOf(encoder.encode(mac_data));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
    }

	public static String encodeURIComponent(String s)
	{
		String result = null;
		try
		{
			result = URLEncoder.encode(s, "UTF-8")
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
		catch (UnsupportedEncodingException e)
		{
			result = s;
		}
		return result;
	}
	public static byte[] hmacSha512(String value, String key){
		try {
			SecretKeySpec keySpec = new SecretKeySpec(
					key.getBytes(DEFAULT_ENCODING), HMAC_SHA512);
			Mac mac = Mac.getInstance(HMAC_SHA512);
			mac.init(keySpec);

			final byte[] macData = mac.doFinal( value.getBytes( ) );
			byte[] hex = HexUtils.toHexString(macData).getBytes();
			return hex;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String asHex(byte[] bytes){
		Base64.Encoder encoder = Base64.getEncoder();
		return new String(encoder.encode(bytes));
	}

	public static String getTimeStamp() {
		long timestamp = System.currentTimeMillis();
		return "timestamp=" + String.valueOf(timestamp);
	}

    public static String bin2hex(byte[] data) {
    	return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    public static String mapToQueryString(Map<String, String> map) {
		StringBuilder string = new StringBuilder();
	
		if (map.size() > 0) {
		    string.append("?");
		}
	
		for (Entry<String, String> entry : map.entrySet()) {
		    string.append(entry.getKey());
		    string.append("=");
		    string.append(entry.getValue());
		    string.append("&");
		}
	
		return string.toString();
    }


}
