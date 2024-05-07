package CoinMerge.coinMergeSpring.news.service;

import CoinMerge.coinMergeSpring.news.dto.newsDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class newsService {
    private static String clientId = "wW0mfl8NyayYnJSC8jVw";
    private static String clientSecret = "Q5HUrwNIMW";

    private static String newsApi(String search) throws IOException, InterruptedException, ParseException, JSONException {
        //uri를 만들기 위한 함수
        String text = null;
        try {
            text = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text
                + "&display=100&start=1&sort=date";

        return apiURL;
    }

    public static ArrayList<newsDTO> callNewsGetter(String search) throws JSONException, IOException, ParseException, InterruptedException {
        //api를 호출해서 뉴스를 받아온다.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(newsApi(search)))//call 함수로 uri를 설정
                .GET()
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<newsDTO> news = parser(response);//response로 받은 내용을 파싱해서 배열에 담는다.

        return news;
    }

    private static ArrayList<newsDTO> parser(HttpResponse<String> response) throws JSONException, IOException {
        //newsDTO를 만들어서 배열에 넣는디.
        JSONObject obj= new JSONObject(response.body());
        JSONArray arr = obj.getJSONArray("items");

        ArrayList<newsDTO>news = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject temp = arr.getJSONObject(i);
            String title = (String) temp.get("title");//제목
            String content = getNewsContent((String) temp.get("link"));//뉴스의 본문을 가져온다.
            if(content.length()==0)continue;
            String link = (String) temp.get("link");//뉴스의 링크를 가져온다.
            String date = (String) temp.get("pubDate");//뉴스 등록 시간
            news.add(new newsDTO(title,content,link,date));
        }
        return news;
    }

    private static String getNewsContent(String newsUrl) throws IOException {
        //네이버 news의 링크를 타고 들어가서 본문을 읽어온다.
        URL url = new URL(newsUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //해당 링크로 받아와서 response에 저장
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //response에서 본문 읽어오기
        Document doc = Jsoup.parse(response.toString());
        Elements bodyElement = doc.getElementsByClass("go_trans _article_content");

        return bodyElement.text().toString();
    }


}
