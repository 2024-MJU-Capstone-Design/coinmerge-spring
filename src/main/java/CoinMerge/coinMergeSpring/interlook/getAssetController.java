package CoinMerge.coinMergeKNKK.interlook;

import CoinMerge.coinMergeKNKK.interlook.ApiClient.binance_Api_Client;
import CoinMerge.coinMergeKNKK.interlook.ApiClient.bithumb_Api_Client;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@Getter @Setter
@Slf4j
public class getAssetController {

    ArrayList<assetCoinDto>assetCoinDtos = new ArrayList<>();

    @RequestMapping("/getCoin")
    public void getCoin(/*@RequestParam String connectKey,
                       @RequestParam String secretKey,*/
                       Model model) throws IOException, ParseException {
        //Api_Client apiClient = new Api_Client(connectKey, secretKey);
        bithumb_Api_Client apiClient = new bithumb_Api_Client("de4c9a0003e507d90a166c0057091d6d",
                "6646dfde6fe6c101835f760cf1f09826");


        HashMap<String,String>rgParams = new HashMap<String, String>();
        rgParams.put("currency", "ALL");
        String result = null;
        try {
            result = apiClient.callApi("/info/balance", rgParams);
            log.info("mssg","결과값 : "+result);
            System.out.println(result);
            model.addAttribute("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bithumbParser(result);

        //return "coin";
    }

    @PostMapping("/binance_getCoin")
    public void binanceGetCoin() throws NoSuchAlgorithmException, InvalidKeyException, ParseException {
        binance_Api_Client apiClient = new binance_Api_Client("vmPUZE6mv9SD5VNHk4HlWFsOr6aKE2zvsw0MuIgwCIPy6utIco14y7Ju91duEh8A",
                "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j");

        String result = null;
        try {
            result = apiClient.callApi();
            log.info("mssg","결과값 : "+result);
            System.out.println(result);
            //model.addAttribute("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject.get("balances"));
    }

    private void bithumbParser(String result) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject obj=(JSONObject)jsonParser.parse(result);
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
    }
}
