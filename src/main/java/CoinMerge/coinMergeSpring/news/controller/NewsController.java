package CoinMerge.coinMergeSpring.news.controller;

import CoinMerge.coinMergeSpring.news.domain.entity.News;
import CoinMerge.coinMergeSpring.news.service.newsService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
@Getter @Setter
@Slf4j
public class NewsController {

    @Autowired
    newsService newsService;

    @GetMapping("/news")
    public ResponseEntity<List<News>> getNews(HttpServletRequest request) throws IOException, InterruptedException, ParseException, JSONException {
        String coinName  = request.getParameter("token");

        List<News> news = newsService.callNewsGetter(coinName);

        return ResponseEntity.ok(news);
    }
}
