package CoinMerge.coinMergeSpring.interlock.ApiClient;

import CoinMerge.coinMergeSpring.interlock.assetCoinDto;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface apiClient {
    String api_url = null;
    String api_key = null;
    String api_secret = null;
    public String callApi() throws Exception;
    public ArrayList<assetCoinDto>parser(String result) throws ParseException;
}
