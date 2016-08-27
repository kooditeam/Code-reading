
package codereading.service;

import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.User;
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

    @Autowired
    private UserService userService;

    public QuestionSeries createQuestionToSeries(Long seriesId, Question question,
                                                 String studentNumber) {

        QuestionSeries series = questionSeriesRepository.findOne(seriesId);
        User user = userService.getOrCreateUser(studentNumber);

        addQuestionToItsAnswerOptions(question);
        question.setQuestionSeries(series);
        question.setCreator(user);
        question = questionRepository.save(question);

        series.addQuestion(question);
        return questionSeriesRepository.save(series);
    }

    public List<QuestionSeries> findAll() {
        return questionSeriesRepository.findAll();
    }

    public QuestionSeries save(QuestionSeries series, String studentNumber) {
        if (studentNumberNotValid(studentNumber)) {
            System.out.println("returning null");
            return null;
        }

        User user = userService.getOrCreateUser(studentNumber);

        setCreatorToQuestions(user, series.getQuestions());
        series = questionSeriesRepository.save(series);

        addSeriesToItsQuestions(series);
        return series;
    }

    private void setCreatorToQuestions(User user, List<Question> questions) {
        if (questions == null) {
            return;
        }

        for (Question question : questions) {
            question.setCreator(user);
        }
    }

    private void addSeriesToItsQuestions(QuestionSeries series) {
        if (noQuestionsPosted(series)) {
            return;
        }

        for (Question question : series.getQuestions()) {
            question.setQuestionSeries(series);
            addQuestionToItsAnswerOptions(question);
        }

        questionRepository.save(series.getQuestions());
    }

    private void addQuestionToItsAnswerOptions(Question question) {
        if (noAnswerOptionsPosted(question)) {
            return;
        }

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

    private boolean studentNumberNotValid(String studentNumber) {
        return studentNumber == null || studentNumber.isEmpty();
    }
}