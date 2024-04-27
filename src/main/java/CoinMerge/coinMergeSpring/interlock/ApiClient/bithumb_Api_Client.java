package CoinMerge.coinMergeSpring.interlock.ApiClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import CoinMerge.coinMergeSpring.interlock.assetCoinDto;
import CoinMerge.coinMergeSpring.interlock.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@SuppressWarnings("unused")
@Slf4j
public class bithumb_Api_Client implements apiClient{
    protected String api_url = "https://api.bithumb.com";
    protected String api_key;
    protected String api_secret;

	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String HMAC_SHA512 = "HmacSHA512";

    public bithumb_Api_Client(String api_key, String api_secret) {
		this.api_key = api_key;
		this.api_secret = api_secret;
    }

	public String request(String strHost,  HashMap<String, String> httpHeaders) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.bithumb.com/info/balance"))
				.header("accept", "application/json")
				.header("content-type", "application/x-www-form-urlencoded")
				.header("api-client-type", "2")
				.header("Api-Key", api_key)
				.header("Api-Nonce", httpHeaders.get("Api-Nonce"))
				.header("Api-Sign", httpHeaders.get("Api-Sign"))
				.method("POST", HttpRequest.BodyPublishers.ofString("currency=ALL"))
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();

	}

	/*private String request(String strHost, HashMap<String, String> rgParams,  HashMap<String, String> httpHeaders) {
		String response = "";

		BHttpRequest = new BHttpRequest();
		Api
		bithumbHttpRequest request = null;

		request = new bithumbHttpRequest(strHost, "POST");
		httpHeaders.put("api-client-type", "2");
		request.headers(httpHeaders);
		request.form(rgParams);

		response = request.body();
		request.disconnect();
		return response;
	}*/

    private HashMap<String, String> getHttpHeaders(String endpoint, HashMap<String, String> rgData, String apiKey, String apiSecret) {
		//빗썸 api 요청에 필요한 헤더들을 여기서 생성
		String strData = util.mapToQueryString(rgData).replace("?", "");
		String nNonce = String.valueOf(System.currentTimeMillis());
		
		strData = strData.substring(0, strData.length()-1);


		System.out.println("1 : " + strData);
		strData = util.encodeURIComponent(strData);
		System.out.println("2 : " + strData);
		HashMap<String, String> array = new HashMap<String, String>();
	
		
		String str = endpoint + ";"	+ strData + ";" + nNonce;
        String encoded = util.asHex(util.hmacSha512(str, apiSecret));
		System.out.println("strData was: " + str);
		System.out.println("apiSecret was: " + apiSecret);
		System.out.println("apiKey was: " + apiKey);
		array.put("Api-Key", apiKey);
		array.put("Api-Sign", encoded);
		array.put("Api-Nonce", String.valueOf(nNonce));
		return array;
    }

    @SuppressWarnings("unchecked")
    public String callApi() throws IOException, InterruptedException {
		String endpoint = "/info/balance";
		String rgResultDecode = "";
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("endpoint", endpoint);
		rgParams.put("currency", "ALL");


		String api_host = api_url + endpoint;
		HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, rgParams, api_key, api_secret);
	
		//rgResultDecode = request(api_host,rgParams, httpHeaders);
		rgResultDecode = request(api_host, httpHeaders);
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

	public ArrayList<assetCoinDto> parser(String result) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject obj=(JSONObject)jsonParser.parse(result);
		ArrayList<assetCoinDto>assetCoinDtos = new ArrayList<>();

		System.out.println("2" + obj.get("data"));
		if(!obj.get("status").equals("0000"))log.info("요청 오류");
		JSONObject obj2 = (JSONObject) obj.get("data");
		for (Object o : obj2.keySet()) {
			String value = obj2.get(o).toString();
			String key = (String) o;
			double total = Double.parseDouble(value);
			if(total == 0)continue;

			String[]arr = key.split("_");

			//String name = arr[arr.length-1];
			if(arr[0].equals("total")){
				System.out.println(value);
				assetCoinDtos.add(new assetCoinDto(key.substring(6), total));
			}
		}

		for (assetCoinDto assetCoinDto : assetCoinDtos) {
			System.out.println(assetCoinDto);
		}
		return assetCoinDtos;
	}
}
