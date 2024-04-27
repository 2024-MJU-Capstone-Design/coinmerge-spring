package CoinMerge.coinMergeSpring.interlock.ApiClient;

import CoinMerge.coinMergeSpring.interlock.assetCoinDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static CoinMerge.coinMergeSpring.interlock.util.*;

public class binance_Api_Client implements apiClient {

    protected String api_url = "https://api.binance.com";
    protected String api_key;
    protected String api_secret;

    public binance_Api_Client(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
    }

    public String callApi() throws Exception {

        String httpMethod = "GET";
        String urlPath = "/api/v3/account";
        HashMap<String,String> parameters = new HashMap<String,String>();
        String queryPath = "";
        String signature = "";
        queryPath += getTimeStamp();
        signature = getSignature(queryPath);

        queryPath += "&signature=" + signature;

        URL obj = new URL(api_url + urlPath + "?" + queryPath);
        System.out.println("url:" + obj.toString());

        return request(obj, httpMethod);
    }

    @Override
    public ArrayList<assetCoinDto> parser(String result) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject obj=(JSONObject)jsonParser.parse(result);
        ArrayList<assetCoinDto>assetCoinDtos = new ArrayList<>();

        System.out.println("2" + obj.get("data"));

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

    public String getSignature(String data ) {
        String key = api_secret;
        byte[] hmacSha256 = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return bytesToHex(hmacSha256);
    }



    private String request(URL obj, String httpMethod) throws Exception {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        if (httpMethod != null) {
            con.setRequestMethod(httpMethod);
        }
        //add API_KEY to header content
        con.setRequestProperty("X-MBX-APIKEY", api_key);
        StringBuffer response = new StringBuffer();

        int responseCode = con.getResponseCode();
        BufferedReader in;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(
                    con.getErrorStream()));
        }
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }


}
