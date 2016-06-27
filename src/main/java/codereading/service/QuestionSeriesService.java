
package codereading.service;

import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionSeriesService {
    
    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public QuestionSeries createQuestionToSeries(Long seriesId, Question question) {
        QuestionSeries series = questionSeriesRepository.findOne(seriesId);
        questionRepository.save(question);
        
        series.addQuestion(question);
        series = questionSeriesRepository.save(series);
        return series;
    }

    public List<QuestionSeries> findAll() {
        return questionSeriesRepository.findAll();
    }

    public QuestionSeries save(QuestionSeries series) {
        return questionSeriesRepository.save(series);
    }
}
