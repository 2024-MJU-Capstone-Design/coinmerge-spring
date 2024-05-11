package CoinMerge.coinMergeSpring.news.controller;

import CoinMerge.coinMergeSpring.news.dto.newsDTO;
import CoinMerge.coinMergeSpring.news.service.newsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.ArrayList;

import static CoinMerge.coinMergeSpring.news.service.newsService.callNewsGetter;

@Controller
@Getter @Setter
@Slf4j
public class getNewsController {


    @Autowired private CoinMerge.coinMergeSpring.news.domain.repository.newsRepository newsRepository;
    @Autowired
    newsService newsService;

    @RequestMapping("/search_news")
    public String getNews(HttpServletRequest request) throws IOException, InterruptedException, ParseException, JSONException {
        //String coinName = "비트코인";
        String coinName  = request.getParameter("coinName");

        ArrayList<newsDTO> searchResult = newsService.callNewsGetter(coinName);//news를 배열형식으로 받아옴
        //System.out.println(searchResult);
        for (newsDTO newsDTO : searchResult) {
            System.out.println(newsDTO);
            newsRepository.save(newsDTO);
        }

        return "news";
    }


}
