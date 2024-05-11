package CoinMerge.coinMergeSpring.news.controller;

import CoinMerge.coinMergeSpring.news.domain.repository.newsRepository;
import CoinMerge.coinMergeSpring.news.dto.newsDTO;
import CoinMerge.coinMergeSpring.news.service.newsService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;


@Transactional
class getNewsControllerTest {
    @Autowired
    newsRepository newsRepository;
    @Autowired
    newsService newsService;

    @Test
    public void 저장() throws JSONException, IOException, ParseException, InterruptedException {

        ArrayList<newsDTO> searchResult = newsService.callNewsGetter("비트코인");//news를 배열형식으로 받아옴


        for (newsDTO newsDTO : searchResult) {
            System.out.println(newsDTO);
            newsRepository.save(newsDTO);
        }

        Assertions.assertThat(newsRepository).isNotNull();
    }



}