package CoinMerge.coinMergeKNKK.interlook.ApiClient;
import java.io.IOException;
import java.util.HashMap;

import CoinMerge.coinMergeKNKK.interlook.coinHttpRequest;
import CoinMerge.coinMergeKNKK.interlook.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import static CoinMerge.coinMergeKNKK.interlook.util.*;


@SuppressWarnings("unused")
public class bithumb_Api_Client {
    protected String api_url = "https://api.bithumb.com";
    protected String api_key;
    protected String api_secret;
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String HMAC_SHA512 = "HmacSHA512";

    public bithumb_Api_Client(String api_key, String api_secret) {
	this.api_key = api_key;
	this.api_secret = api_secret;
    }

	/*public String apiCall(){
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.bithumb.com/info/balance"))
				.header("accept", "application/json")
				.header("content-type", "application/x-www-form-urlencoded")
				.header("Api-Key", "사용자 Access Key")
				.header("Api-Nonce", "현재시각(ms)") usec으로 만듦
				.header("Api-Sign", "상세 가이드 참고") End Point + Request Parameter + Api-Nonce +
														사용자 Secret Key를 조합하여 인코딩한 값
												Request Parameter 조합 -> End Point + Request Parameter + Api-Nonce 조합
												-> 	HmacSha512 알고리즘으로 인코딩 -> Base64 인코딩 -> api-sign 완성
				.method("POST", HttpRequest.BodyPublishers.ofString("currency=BTC"))
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}*/

    private String request(String strHost, String strMemod, HashMap<String, String> rgParams,  HashMap<String, String> httpHeaders) {
    	String response = "";

		if (strHost.startsWith("https://")) {
		    coinHttpRequest request = coinHttpRequest.get(strHost);
		    // Accept all certificates
		    request.trustAllCerts();
		    // Accept all hostnames
		    request.trustAllHosts();
		}
		coinHttpRequest request = null;

		request = new coinHttpRequest(strHost, "POST");
		request.readTimeout(10000);
		//System.out.println("POST ==> " + request.url());

		//if (httpHeaders != null && !httpHeaders.isEmpty()) {
			httpHeaders.put("api-client-type", "2");
			request.headers(httpHeaders);
			//System.out.println(httpHeaders.toString());
		//}
		//if (rgParams != null && !rgParams.isEmpty()) {
			request.form(rgParams);
			//System.out.println("rgParams was: " + rgParams.toString());
		//}

		response = request.body();
		request.disconnect();
		//System.out.println("Response was: " + response);
		return response;
    }
    


    private HashMap<String, String> bithumbGetHttpHeaders(String endpoint, HashMap<String, String> rgData, String apiKey, String apiSecret) {
		//빗썸 api 요청에 필요한 헤더들을 여기서 생성
		String strData = util.mapToQueryString(rgData).replace("?", "");
		String nNonce = String.valueOf(System.currentTimeMillis());
		
		strData = strData.substring(0, strData.length()-1);


		System.out.println("1 : " + strData);
		strData = encodeURIComponent(strData);
		HashMap<String, String> array = new HashMap<String, String>();
	
		
		String str = endpoint + ";"	+ strData + ";" + nNonce;
        String encoded = util.asHex(hmacSha512(str, apiSecret));

		System.out.println("strData was: " + str);
		System.out.println("apiSecret was: " + apiSecret);
		array.put("Api-Key", apiKey);
		array.put("Api-Sign", encoded);
		array.put("Api-Nonce", String.valueOf(nNonce));
		return array;
    }

    @SuppressWarnings("unchecked")
    public String callApi(String endpoint, HashMap<String, String> params) {
		String rgResultDecode = "";
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("endpoint", endpoint);
	
		if (params != null) {
		    rgParams.putAll(params);
		}
	
		String api_host = api_url + endpoint;
		HashMap<String, String> httpHeaders = bithumbGetHttpHeaders(endpoint, rgParams, api_key, api_secret);
	
		rgResultDecode = request(api_host, "POST", rgParams, httpHeaders);
	
		if (!rgResultDecode.startsWith("error")) {
		    HashMap<String, String> result;
		    try {
				result = new ObjectMapper().readValue(rgResultDecode,
					HashMap.class);

				System.out.println("===============");
				System.out.println(result.get("status"));
		    } catch (IOException e) {
				e.printStackTrace();
		    }
		}
		return rgResultDecode;
    }
}
