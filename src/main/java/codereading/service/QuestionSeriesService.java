
package codereading.service;

import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.SeriesRequestWrapper;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;

import codereading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionSeriesService {

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    public SeriesRequestWrapper createQuestionToSeries(Long seriesId, Question question,
                                                       String studentNumber,
                                                       List<AnswerOption> answerOptions) {

        if (seriesId == null || question == null || studentNumberNotValid(studentNumber)
                || !answerOptionsValid(answerOptions)) {
            return null;
        }

        QuestionSeries series = questionSeriesRepository.findOne(seriesId);
        if (series == null) {
            return null;
        }

        question = saveQuestionWithSeriesAndUserInfo(question, series, studentNumber);
        saveAnswerOptionsWithQuestionInfo(answerOptions, question);

        return new SeriesRequestWrapper(series, studentNumber, question, answerOptions);
    }

    private boolean answerOptionsValid(List<AnswerOption> options) {
        return options != null && options.size() >= 2;
    }

    public SeriesRequestWrapper createNewSeries(QuestionSeries series,
                                          Question question,
                                          String studentNumber,
                                          List<AnswerOption> answerOptions) {
        if (series == null || studentNumberNotValid(studentNumber)) {
            return null;
        }
        series = questionSeriesRepository.save(series);

        if (question != null && answerOptionsValid(answerOptions)) {
            question = saveQuestionWithSeriesAndUserInfo(question, series, studentNumber);
            saveAnswerOptionsWithQuestionInfo(answerOptions, question);
        }

        return new SeriesRequestWrapper(series, studentNumber, question, answerOptions);
    }

    private boolean studentNumberNotValid(String studentNumber) {
        return studentNumber == null || studentNumber.isEmpty();
    }

    private Question saveQuestionWithSeriesAndUserInfo(Question question,
                                                   QuestionSeries series,
                                                   String studentNumber) {

        User user = userService.getExistingOrNewUser(studentNumber);

        question.setCreator(user);
        question.setQuestionSeries(series);
        return questionRepository.save(question);
    }

    private void saveAnswerOptionsWithQuestionInfo(List<AnswerOption> options, Question question) {
        for (AnswerOption option : options) {
            option.setQuestion(question);
            answerOptionRepository.save(option);
        }
    }
}