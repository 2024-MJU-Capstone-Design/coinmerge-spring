package CoinMerge.coinMergeSpring.common.utils;

import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceSnapshotDto;
import CoinMerge.coinMergeSpring.asset.dto.binance.BinanceTransactionDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


  public static List<BinanceTransactionDto> binanceTransactionHistoryParser(Map<String, LinkedHashMap> response) {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.valueToTree(response);
    JsonNode listNode = rootNode.get("list");

    List<BinanceTransactionDto> tradeFlows = new ArrayList<>();
    if (listNode != null && listNode.isArray()) {
      tradeFlows = objectMapper.convertValue(listNode, new TypeReference<List<BinanceTransactionDto>>() {});
    }

    // Print the list items

    return tradeFlows;
  }

  public static List<BinanceSnapshotDto> binanceSnapshotParser(Map<String, LinkedHashMap> response) {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode3 = objectMapper.valueToTree(response);
    JsonNode listNode3 = rootNode3.get("snapshotVos");
    System.out.println(listNode3);

    JsonNode listNode = listNode3.get("data");
    System.out.println(listNode);

    JsonNode rootNode2 = objectMapper.valueToTree(listNode);
    JsonNode listNode2 = rootNode2.get("balances");
    System.out.println(listNode2);
    List<BinanceSnapshotDto> tradeFlows = new ArrayList<>();
    if (listNode != null && listNode2.isArray()) {
      tradeFlows = objectMapper.convertValue(listNode2, new TypeReference<List<BinanceSnapshotDto>>() {});
    }

    // Print the list items

    return tradeFlows;
  }

  public static List<BinanceSnapshotDto> snapshotParser(long timestamp, Map<String, LinkedHashMap> response) {
    List<BinanceSnapshotDto> out = new ArrayList<>();
    if (response != null && response.containsKey("snapshotVos")) {
      List<LinkedHashMap> snapshotVos = (List<LinkedHashMap>) response.get("snapshotVos");
      for (LinkedHashMap snapshotVo : snapshotVos) {
        LinkedHashMap data = (LinkedHashMap) snapshotVo.get("data");
        if(snapshotVo.get("updateTime") != null) timestamp = (long) snapshotVo.get("updateTime");
        if (data != null && data.containsKey("balances")) {
          List<LinkedHashMap> balances = (List<LinkedHashMap>) data.get("balances");
          for (LinkedHashMap balanceMap : balances) {
            String asset = (String) balanceMap.get("asset");
            String free = balanceMap.get("free").toString();
            String locked = balanceMap.get("locked").toString();
            if(!free.equals("0")){
              BinanceSnapshotDto dto = new BinanceSnapshotDto(asset, free, locked, timestamp);
              if(!out.contains(dto)) out.add(dto);
            }
          }
        }
      }
    }
    return out;
  }

}