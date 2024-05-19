package CoinMerge.coinMergeSpring.schedular;

import CoinMerge.coinMergeSpring.news.domain.entity.News;
import CoinMerge.coinMergeSpring.news.domain.repository.NewsRepository;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService {
    @Autowired
    CoinMerge.coinMergeSpring.news.service.newsService newsService;
    @Autowired
    NewsRepository newsRepository;

    String[]token = {"비트코인", "이더리움", "리플"};

    @Scheduled(cron="*/30 * * * * *")
    public void scheduleRun() throws JSONException, IOException, ParseException, InterruptedException {
        List<News> out = new ArrayList<>();
        for (String s : token) {
            List<News> news = newsService.callNewsGetter(s);
            out.addAll(news);
        }


        for (News news : out) {
            if(!newsRepository.existsByTitle(news.getTitle())){
                newsRepository.save(news);
                System.out.println(news);
            }else{
                System.out.println("already in the repository");
            }
        }

    }

}