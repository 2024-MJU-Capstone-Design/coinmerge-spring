package CoinMerge.coinMergeSpring.interlock;

import CoinMerge.coinMergeSpring.interlock.ApiClient.apiClient;
import CoinMerge.coinMergeSpring.interlock.ApiClient.binance_Api_Client;
import CoinMerge.coinMergeSpring.interlock.ApiClient.bithumb_Api_Client;
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

@Controller
@Getter @Setter
@Slf4j
public class getAssetController {


    @RequestMapping("getCoin")
    public void getCoin(@RequestParam String connectKey,
                       @RequestParam String secretKey,
                        @RequestParam String platForm,
            Model model) throws Exception {
        apiClient apiClient;
        if(platForm.equals("bithumb")){
            apiClient = new bithumb_Api_Client(connectKey, secretKey);
        }else{
            apiClient = new binance_Api_Client(connectKey, secretKey);
        }

        String result = null;
        result = apiClient.callApi();
        log.info("mssg","결과값 : "+result);
        System.out.println(result);

        ArrayList<assetCoinDto>assetCoinDtos = apiClient.parser(result);
        model.addAttribute("coinList", assetCoinDtos);
        //return "coin";
    }


    @RequestMapping("/bithumb_getCoin")
    public void bithumbGetCoin(/*@RequestParam String connectKey,
                       @RequestParam String secretKey,*/
                       Model model) throws IOException, ParseException {
        //Api_Client apiClient = new Api_Client(connectKey, secretKey);
        bithumb_Api_Client apiClient = new bithumb_Api_Client("de4c9a0003e507d90a166c0057091d6d",
                "6646dfde6fe6c101835f760cf1f09826");



        String result = null;
        try {
            result = apiClient.callApi();
            log.info("mssg","결과값 : "+result);
            System.out.println(result);
            model.addAttribute("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return "coin";
    }

    @RequestMapping("/binance_getCoin")
    public void binanceGetCoin() throws NoSuchAlgorithmException, InvalidKeyException, ParseException {
        binance_Api_Client apiClient = new binance_Api_Client("4nhwLbKcUQxXGqVFjz8luWXQ87LDOR98pQMgjq5eHQrHZlH1dsG826hoKBn0xb8a",
                "vOM2md4SYqQaY76uvivutcNGqfFnpCfTrigEFrlWH3Ho4EtGciAP92yPVFFt7kPe");

        String result = null;
        try {
            result = apiClient.callApi();
            log.info("mssg","결과값 : "+result);
            System.out.println(result);
            //model.addAttribute("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*private void bithumbParser(String result) throws ParseException {
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
    }*/
}
