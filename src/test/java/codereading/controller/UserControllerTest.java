
package codereading.controller;

import codereading.Main;
import codereading.domain.Answer;
import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.AnswerRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.UserRepository;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertTrue;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    private final String API_URI = "/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    private Gson mapper;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mapper = new Gson();
    }

    @Test
    public void tryingToGetQuestionsForNonExistingUsersReturnsAllQuestionsInTheDatabase() throws Exception {
        saveMultipleQuestions(5);

        MvcResult result = this.mockMvc.perform(get(API_URI + "/099999999/unanswered"))
                .andExpect(status().isOk())
                .andReturn();

        String questionsAsJson =  result.getResponse().getContentAsString();        
        Question[] questions = mapper.fromJson(questionsAsJson, Question[].class);

        assertTrue(questions.length == questionRepository.count());      
    }

    @Test
    public void tryingToQuestionsForAnExistingUserReturnsAllQuestionsWhenUserHasNotAnsweredAny() throws Exception {
        saveMultipleQuestions(5);
        saveUser(null);

        MvcResult result = this.mockMvc.perform(get(API_URI + "/099999999/unanswered"))
                .andExpect(status().isOk())
                .andReturn();

        String questionsAsJson = result.getResponse().getContentAsString();
        Question[] questions = mapper.fromJson(questionsAsJson, Question[].class);

        assertTrue(questions.length == questionRepository.count());
    }

    @Test
    public void tryingToGetQuestionsForAnExistingUserReturnsAllTheUnansweredQuestionsWhenUserHasCorrectlyAnsweredSomeQuestionsInDatabase() throws Exception {
        saveMultipleQuestions(5);
        User user = saveUser("088888888");
        answerQuestionsCorrectly(saveMultipleQuestions(2), user);
        
        MvcResult result = this.mockMvc.perform(get(API_URI + "/088888888/unanswered"))
                .andExpect(status().isOk())
                .andReturn();

        String questionsAsJson = result.getResponse().getContentAsString();
        Question[] questions = mapper.fromJson(questionsAsJson, Question[].class);

        assertTrue(questionRepository.count() == 7);
        assertTrue(questions.length == 5);
    }

    @Test
    public void tryingToGetQuestionsForAnExistingUserReturnsAllQuestionsWhenUserHasAnsweredSomeButWrong() throws Exception {
        saveMultipleQuestions(5);
        User user = saveUser("077777777");
        answerQuestionsWrong(saveMultipleQuestions(3), user);

        MvcResult result = this.mockMvc.perform(get(API_URI + "/077777777/unanswered"))
                .andExpect(status().isOk())
                .andReturn();

        String questionsAsJson = result.getResponse().getContentAsString();
        Question[] questions = mapper.fromJson(questionsAsJson, Question[].class);

        assertTrue(questionRepository.count() == 8);
        assertTrue(questions.length == 8);
    }

    @Test
    public void tryingToGetQuestiosForAnExistingUserReturnsUnansweredAndWronglyAnsweredWhenUserHasAnsweredSomeCorrectly() throws Exception {
        saveMultipleQuestions(4);
        User user = saveUser("066666666");
        answerQuestionsWrong(saveMultipleQuestions(3), user);
        answerQuestionsCorrectly(saveMultipleQuestions(2), user);

        MvcResult result = this.mockMvc.perform(get(API_URI + "/066666666/unanswered"))
                .andExpect(status().isOk())
                .andReturn();

        String questionsAsJson = result.getResponse().getContentAsString();
        Question[] questions = mapper.fromJson(questionsAsJson, Question[].class);

        assertTrue(questionRepository.count() == 9);
        assertTrue(questions.length == 7);
    }

    private void answerQuestionsWrong(List<Question> questions, User user) {
        answerQuestions(questions, user, false);
    }

    private void answerQuestionsCorrectly(List<Question> questions, User user) {
        answerQuestions(questions, user, true);
    }

    private void answerQuestions(List<Question> questions, User user, boolean isCorrect) {
        for (Question question : questions) {
            saveAnswer(question, user, isCorrect);
        }
    }

    private List<Question> saveMultipleQuestions(int amount) {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            questions.add(saveQuestion());
        }
        return questions;
    }

    private Question saveQuestion() {
        Question question = new Question();
        return questionRepository.save(question);
    }

    private Answer saveAnswer(Question question, User user, boolean isCorrect) {
        AnswerOption option = saveAnswerOption(question, isCorrect);

        Answer answer = new Answer();
        answer.setAnswerOption(option);
        answer.setAnswerer(user);

        return answerRepository.save(answer);
    }

    private AnswerOption saveAnswerOption(Question question, boolean isCorrect) {
        AnswerOption option = new AnswerOption();
        option.setIsCorrect(isCorrect);
        option.setQuestion(question);
        return answerOptionRepository.save(option);
    }

    private User saveUser(String studentNumber) {
        User user = new User();
        user.setStudentNumber(studentNumber);
        
        return userRepository.save(user);
    }
}
