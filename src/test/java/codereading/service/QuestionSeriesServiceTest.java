
package codereading.service;

import codereading.Main;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@Transactional
public class QuestionSeriesServiceTest {

    @Autowired
    private QuestionSeriesService questionSeriesService;

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void creatingQuestionForSeriesSavesItToTheDatabaseAndToTheSeries() {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitleinService");
        questionSeriesRepository.save(series);

        Question question = new Question();
        question.setTitle("question title1");
        question.setCode("System.out.println()");
        
        series = questionSeriesService.createQuestionToSeries(series.getId(), question);
        
        assertTrue(questionRepository.count() == 1);
        assertTrue(series.getQuestions().size() == 1);

    }
}
