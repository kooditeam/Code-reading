
package codereading.service;

import codereading.Main;
import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.SeriesRequestWrapper;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.*;

import codereading.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Test
    public void creatingSeriesWithoutQuestionSavesTheSeriesToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createNewSeries(series, null, "02312311", answerOptions);

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 0);
    }

    @Test
    public void tryingToCreateSeriesWithNullSeriesDoesNotSaveAnythingToDatabase() throws Exception {
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);
        questionSeriesService.createNewSeries(null, new Question(), "02312312", answerOptions);

        assertTrue(questionSeriesRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);
        assertTrue(userRepository.count() == 0);
    }

    @Test
    public void creatingSeriesWithQuestionSavesBothToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createNewSeries(series, new Question(), "02312313", answerOptions);

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 1);
    }

    @Test
    public void creatingSeriesWithQuestionAddTheSeriesInfoToTheQuestion() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("testTitle1");
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createNewSeries(series, new Question(), "02312314", answerOptions);

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        assertEquals(series.getTitle(), question.getQuestionSeries().getTitle());
    }

    @Test
    public void creatingSeriesWithQuestionAddTheSeriesInfoToItsCreator() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("testTitle1");
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createNewSeries(series, new Question(), "02312315", answerOptions);

        assertTrue(questionRepository.count() == 1);
        assertTrue(userRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        assertEquals(user.getStudentNumber(), question.getCreator().getStudentNumber());
    }

    @Test
    public void creatingQuestionForExistingSeriesSavesItToTheDatabase() {
        QuestionSeries series = new QuestionSeries();
        questionSeriesRepository.save(series);
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createQuestionToSeries(series.getId(), new Question(), "02312316",
                answerOptions);

        assertTrue(questionRepository.count() == 1);
    }

    @Test
    public void creatingQuestionWithAnswerOptionsForExistingSeriesSavesTheAnswerOptionsToDatabase() {
        QuestionSeries series = new QuestionSeries();
        questionSeriesRepository.save(series);

        Question question = new Question();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(2);

        questionSeriesService.createQuestionToSeries(series.getId(), question, "023123113", answerOptions);

        assertTrue(answerOptionRepository.count() == 2);
    }

    @Test
    public void creatingQuestionWIthAnswerOptionsForExistingSeriesAddsTheQuestionToTheAnswerOptions() {
        QuestionSeries series = new QuestionSeries();
        questionSeriesRepository.save(series);
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(2);

        Question question = new Question();
        question.setTitle("questionTitle");

        questionSeriesService.createQuestionToSeries(series.getId(), question, "02312313", answerOptions);

        assertTrue(answerOptionRepository.count() == 2);
        AnswerOption option1 = answerOptionRepository.findAll().get(0);
        AnswerOption option2 = answerOptionRepository.findAll().get(1);

        assertTrue(questionRepository.count() == 1);
        question = questionRepository.findAll().get(0);

        assertNotNull(option1.getQuestion());
        assertNotNull(option2.getQuestion());
        assertEquals(question.getTitle(), option1.getQuestion().getTitle());
        assertEquals(question.getTitle(), option2.getQuestion().getTitle());
    }


    @Test
    public void creatingQuestionToSeriesAddsTheCreatorInfoToTheQuestion() {
        User user = userRepository.save(new User("041231232"));
        QuestionSeries series = new QuestionSeries();
        questionSeriesRepository.save(series);
        Question question = new Question();
        question.setTitle("testTitle");
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        questionSeriesService.createQuestionToSeries(series.getId(), question, user.getStudentNumber(), answerOptions);

        assertTrue(questionRepository.count() == 1);
        question = questionRepository.findAll().get(0);

        assertNotNull(question.getCreator());
        assertEquals(user.getStudentNumber(), question.getCreator().getStudentNumber());
    }

    @Test
    public void creatingNewQuestionSeriesSavesItToTheDatabase() {
        QuestionSeries series = new QuestionSeries();
        questionSeriesService.createNewSeries(series, null, "011111113", null);

        assertTrue(questionSeriesRepository.count() ==  1);
    }

    @Test
    public void creatingNewSeriesWithQuestionsSavesAlsoTheQuestionsToDatabase() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();

        questionSeriesService.createNewSeries(series, question, "011111112", createAnswerOptionsForQuestion(2));

        assertTrue(questionRepository.count() == 1);
    }

    @Test
    public void creatingNewSeriesWithQuestionSavesAlsoTheAnswerOptionsToDatabase() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();

         questionSeriesService.createNewSeries(series, question, "011111114", createAnswerOptionsForQuestion(2));

        assertTrue(answerOptionRepository.count() == 2);
    }

    @Test
    public void creatingNewSeriesWithQuestionAddsTheSeriesToTheQuestion() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();

        questionSeriesService.createNewSeries(series, question, "011111115", createAnswerOptionsForQuestion(2));

        assertTrue(questionRepository.count() == 1);

        assertTrue(questionSeriesRepository.count() == 1);
        series = questionSeriesRepository.findAll().get(0);

        assertNotNull(question.getQuestionSeries());
        assertEquals(series.getId(), question.getQuestionSeries().getId());
    }

    @Test
    public void creatingNewSeriesWithQuestionThatHasAnswerOptionsAddsTheQuestionToTheOptions() {
        QuestionSeries series = new QuestionSeries();
        Question question = new Question();

        questionSeriesService.createNewSeries(series, question, "011111115", createAnswerOptionsForQuestion(2));

        assertTrue(answerOptionRepository.count() == 2);
        List<AnswerOption> options = answerOptionRepository.findAll();

        assertTrue(questionRepository.count() == 1);
        question = questionRepository.findAll().get(0);

        for (AnswerOption option : options) {
            assertNotNull(option.getQuestion());
            assertEquals(question.getId(), option.getQuestion().getId());
        }
    }

    @Test
    public void creatingSeriesWithoutAnyQuestionsSavesTheSeriesToTheDatabase() {
        QuestionSeries series = new QuestionSeries();
        questionSeriesService.createNewSeries(series, null, "011111115", null);

        assertTrue(questionSeriesRepository.count() == 1);
    }

    @Test
    public void creatingNewSeriesWithoutQuestionsReturnsObjectWithTheSeriesAndUsernameInfo() {
        QuestionSeries series = new QuestionSeries();
        SeriesRequestWrapper wrapper = questionSeriesService.createNewSeries(series, null, "011111116", null);

        assertNotNull(wrapper);
        assertNotNull(wrapper.getQuestionSeries());
        assertNotNull(wrapper.getQuestionSeries().getId());
        assertEquals("011111116", wrapper.getStudentNumber());
        assertNull(wrapper.getQuestion());
        assertNull(wrapper.getAnswerOptions());
    }

    @Test
    public void creatingNewSeriesWithQuestionsReturnsObjectWithTheSeriesUserNameQuestionAndAnswerOptionsInfo() {
        QuestionSeries series = new QuestionSeries();
        SeriesRequestWrapper wrapper = questionSeriesService.createNewSeries(series, new Question(), "011111116", createAnswerOptionsForQuestion(2));

        assertNotNull(wrapper);
        assertNotNull(wrapper.getQuestionSeries());
        assertNotNull(wrapper.getQuestionSeries().getId());
        assertEquals("011111116", wrapper.getStudentNumber());

        assertNotNull(wrapper.getQuestion());
        assertNotNull(wrapper.getQuestion().getId());

        assertNotNull(wrapper.getAnswerOptions());
        assertTrue(wrapper.getAnswerOptions().size() == 2);
    }

    private List<AnswerOption> createAnswerOptionsForQuestion(int amountOfOptions) {
        List<AnswerOption> options = new ArrayList<>();
        for (int i = 1; i <= amountOfOptions; i++) {
            options.add(new AnswerOption());
        }

        return options;
    }
}