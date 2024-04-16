package CoinMerge.coinMergeKNKK.interlook.ApiClient;

import CoinMerge.coinMergeKNKK.interlook.coinHttpRequest;
import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class binance_Api_Client {

    protected String api_url = "https://api.binance.com";
    //protected String api_url = "https://testnet/api.binance.com";
    protected String api_key;
    protected String api_secret;

    public binance_Api_Client(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
    }


    /*public String callApi() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secKey = new SecretKeySpec(api_secret.getBytes(), "HmacSHA256");
        hmacSha256.init(secKey);//seckey 암호화

        String actualSign = new String(HexUtils.fromHexString(hmacSha256.doFinal(queryString.getBytes())));
        queryString = queryString + "&signature=" + actualSign;

        String serverUrl = "https://api.binance.com";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(serverUrl + "/api/v3/account?" + queryString);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("X-MBX-APIKEY", accessKey);

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        String entityString = EntityUtils.toString(entity, "UTF-8");

        JSONParser jsonParser = new JSONParser();//여기부터 파싱
        JSONObject account = (JSONObject) jsonParser.parse(entityString);

        JSONArray balances = (JSONArray) account.get("balances");
    }*/
    public String callApi() throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        // 서명 계산
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secKey = new SecretKeySpec(api_secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secKey);

        String queryString = Long.toString(System.currentTimeMillis()); // 쿼리 문자열이 여기에 있어야 함
        // 서명 계산 및 추가
        byte[] actualSignBytes = hmacSha256.doFinal(queryString.getBytes(StandardCharsets.UTF_8));
        //String actualSign = java.util.Hex.encodeHexString(actualSignBytes);
        String actualSign = HexUtils.toHexString(actualSignBytes);
        queryString = "timestamp=" + queryString + "&signature=" + actualSign;

        // API 호출
        URI uri = URI.create(api_url + "/api/v3/account?" + queryString);
        ///sapi/v3/asset/getUserAsset
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("X-MBX-APIKEY", api_key)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 처리
        String entityString = response.body();
        /*JSONObject account = new JSONObject(entityString);
        //JSONArray balances = account.getJSONArray("balances");
        HashMap<String, String> result = null;
        try {
            result = new ObjectMapper().readValue(response.body(),
                    HashMap.class);

            System.out.println("==== ��� ��� ====");
            System.out.println(result.get("status"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        return entityString;
    }


}
