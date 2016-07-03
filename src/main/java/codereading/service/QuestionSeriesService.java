
package codereading.service;

import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.repository.AnswerOptionRepository;
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
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    /**
     * Saves a newly made question to an already existing question series
     * @param seriesId
     * @param question
     * @return the questionseries containing the newly made question
     */
    public QuestionSeries createQuestionToSeries(Long seriesId, Question question) {
        QuestionSeries series = questionSeriesRepository.findOne(seriesId);

        addQuestionToItsAnswerOptions(question);
        question.setQuestionSeries(series);
        questionRepository.save(question);

        series.addQuestion(question);
        return questionSeriesRepository.save(series);
    }

    public List<QuestionSeries> findAll() {
        return questionSeriesRepository.findAll();
    }

    /**
     * Saves the series to the database, also giving the info on the series
     * itself to any questions that were posted with it.
     * @param series A newly made QuestionSeries object
     * @return the saved series
     */
    public QuestionSeries save(QuestionSeries series) {
        series = questionSeriesRepository.save(series);

        addSeriesToItsQuestions(series);
        return series;
    }

    private void addSeriesToItsQuestions(QuestionSeries series) {
        if (noQuestionsPosted(series)) return;

        for (Question question : series.getQuestions()) {
            question.setQuestionSeries(series);
            addQuestionToItsAnswerOptions(question);
        }

        questionRepository.save(series.getQuestions());
    }

    private void addQuestionToItsAnswerOptions(Question question) {
        if (noAnswerOptionsPosted(question)) return;

        for (AnswerOption answerOption : question.getAnswerOptions()) {
            answerOption.setQuestion(question);
        }

        answerOptionRepository.save(question.getAnswerOptions());
    }

    private boolean noAnswerOptionsPosted(Question question) {
        return question.getAnswerOptions() == null;
    }

    private boolean noQuestionsPosted(QuestionSeries series) {
        return series.getQuestions() == null;
    }
}
