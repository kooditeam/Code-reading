
package codereading.service;

import codereading.Main;
import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class QuestionSeriesServiceTest {

    @Autowired
    private QuestionSeriesService questionSeriesService;

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Test
    public void creatingQuestionForExistingSeriesSavesItToTheDatabase() {
        Long questionCount = questionRepository.count();

        QuestionSeries series = saveQuestionSeries();
        Question question = createUnsavedQuestion(0);

        questionSeriesService.createQuestionToSeries(series.getId(), question, "02312311");

        assertTrue(questionRepository.count() == questionCount + 1);
    }

    @Test
    public void creatingQuestionForExistingSeriesSavesItToTheQuestionSeries() {
        QuestionSeries series = saveQuestionSeries();
        Question question = new Question();
        question.setCreator(new User("032111133"));
        question = questionRepository.save(question);

        series = questionSeriesService.createQuestionToSeries(series.getId(), question, "02312312");

        assertNotNull(series.getQuestions());
        assertTrue(series.getQuestions().size() == 1);
        System.out.println("series questions are: " + series.getQuestions());
        System.out.println("question id is: " + question.getId());
        assertEquals(question.getId(), series.getQuestions().get(0).getId());
    }

    @Test
    public void creatingQuestionForExistingSeriesSavesTheSeriesToTheNewlyMadeQuestion() {
        QuestionSeries series = saveQuestionSeries();
        Question question = new Question();

        series = questionSeriesService.createQuestionToSeries(series.getId(), question, "02312313");

        assertNotNull(question.getQuestionSeries());
        assertEquals(series.getId(), question.getQuestionSeries().getId());
    }

    @Test
    public void creatingQuestionWithAnswerOptionsForExistingSeriesSavesTheAnswerOptionsToDatabase() {
        Long answerOptionCount = answerOptionRepository.count();

        QuestionSeries series = saveQuestionSeries();
        Question question = new Question();

        question = createAnswerOptionsForQuestion(question, 2);

        questionSeriesService.createQuestionToSeries(series.getId(), question, "023123113");

        assertTrue(answerOptionRepository.count() == answerOptionCount + 2);
    }

    @Test
    public void creatingQuestionWithAnswerOptionsForExistingSeriesAddsTheAnswerOptionsToTheQuestion() {
        QuestionSeries series = saveQuestionSeries();
        Question question = new Question();

        question = createAnswerOptionsForQuestion(question, 2);

        questionSeriesService.createQuestionToSeries(series.getId(), question, "12453212");

        assertNotNull(question.getAnswerOptions());
        assertTrue(question.getAnswerOptions().size() == 2);
    }

    @Test
    public void creatingQuestionWIthAnswerOptionsForExistingSeriesAddsTheQuestionToTheAnswerOptions() {        
        QuestionSeries series = saveQuestionSeries();

        Question question = saveQuestion(5);

        question = createAnswerOptionsForQuestion(question, 2);

        questionSeriesService.createQuestionToSeries(series.getId(), question, "02312313");

        AnswerOption option1 = question.getAnswerOptions().get(0);
        AnswerOption option2 = question.getAnswerOptions().get(1);

        assertNotNull(option1.getQuestion());
        assertNotNull(option1.getQuestion());
        assertEquals(question.getId(), option1.getQuestion().getId());
        assertEquals(question.getId(), option2.getQuestion().getId());
    }

    @Test
    public void creatingNewQuestionSeriesSavesItToTheDatabase() {
        Long seriesCount = questionSeriesRepository.count();

        QuestionSeries series = new QuestionSeries();
        questionSeriesService.save(series, "011111113");

        assertTrue(questionSeriesRepository.count() == seriesCount + 1);
    }

    @Test
    public void creatingNewSeriesWithQuestionsSavesAlsoTheQuestionsToDatabase() {
        Long questionCount = questionRepository.count();

        QuestionSeries series = new QuestionSeries();
        series.addQuestion(new Question());
        series.addQuestion(new Question());

        questionSeriesService.save(series, "011111112");

        assertTrue(questionRepository.count() == questionCount + 2);
    }

    @Test
    public void creatingNewSeriesWithQuestionsThatHaveAnswerOptionsSavesAlsoTheAnswerOptionsToDatabase() {
        Long optionCount = answerOptionRepository.count();

        QuestionSeries series = new QuestionSeries();
        Question question = new Question();
        series.addQuestion(question);
        createAnswerOptionsForQuestion(question, 2);

        questionSeriesService.save(series, "011111114");
        assertTrue(answerOptionRepository.count() == optionCount + 2);
    }

    @Test
    public void creatingNewSeriesWithQuestionAddsTheSeriesToTheQuestion() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();

        series.addQuestion(question);
        questionSeriesService.save(series, "011111115");

        assertEquals(series.getId(), question.getQuestionSeries().getId());
    }

    @Test
    public void creatingNewSeriesWithQuestionThatHasAnswerOptionsAddsTheQuestionToTheOptions() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();
        createAnswerOptionsForQuestion(question, 2);

        series.addQuestion(question);
        questionSeriesService.save(series, "011111116");

        AnswerOption option1 = question.getAnswerOptions().get(0);
        AnswerOption option2 = question.getAnswerOptions().get(1);

        assertEquals(question.getId(), option1.getQuestion().getId());
        assertEquals(question.getId(), option2.getQuestion().getId());
    }

    private Question createAnswerOptionsForQuestion(Question question, int amountOfOptions) {
        List<AnswerOption> options = new ArrayList<>();
        for (int i = 1; i <= amountOfOptions; i++) {
            options.add(new AnswerOption());
        }

        question.setAnswerOptions(options);
        return question;
    }

    private QuestionSeries saveQuestionSeries() {
        QuestionSeries series = new QuestionSeries();
        series = questionSeriesRepository.save(series);

        return series;
    }

    private Question saveQuestion(int i) {
        Question question = new Question();
        question.setCreator(new User("sjdfja" + i));

        return questionRepository.save(question);
    }

    private Question createUnsavedQuestion(int i) {
        Question question = new Question();
        question.setCreator(new User("03466543" + i));

        return question;
    }
}