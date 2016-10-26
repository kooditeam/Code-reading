package codereading.controller;

import codereading.Main;
import codereading.domain.Answer;
import codereading.domain.AnswerOption;
import codereading.domain.Feedback;
import codereading.domain.Question;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.AnswerRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.UserRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class AnswerControllerTest {

    private final String API_URI = "/answers";

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Gson mapper;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mapper = new Gson();
    }

    @Test
    public void postWithCorrectAnswerInfoSendsFeedback() throws Exception {
        User user = userRepository.save(new User("112233211"));

        Question question = saveQuestion(user);
        AnswerOption answerOption = new AnswerOption();
        answerOption.setIsCorrect(false);
        answerOption.setQuestion(question);
        answerOption = answerOptionRepository.save(answerOption);

        Answer answer = new Answer();
        answer.setAnswerer(user);
        answer.setAnswerOption(answerOption);

        String answerAsJson = mapper.toJson(answer);

        MvcResult result = mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(answerAsJson)).andExpect(status().isOk()).andReturn();

        String feedbackAsJson = result.getResponse().getContentAsString();
        Feedback feedback = mapper.fromJson(feedbackAsJson, Feedback.class);

        assertFalse(feedback.getIsCorrect());

        user = userRepository.findOne(user.getId());
        answer = answerRepository.findAll().get(0);

        assertTrue(answerRepository.count() == 1);
        assertTrue(answer.getAnswerer().getId() == user.getId());
    }

    @Test
    public void postingNewAnswerWithAnswerOptionThatIsNotInDatabaseReturnsBadRequest() throws Exception {
        User user = new User("0555555");
        user = userRepository.save(user);

        Question question = new Question();
        question.setCreator(user);
        question = questionRepository.save(question);

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);

        Answer answer = new Answer();
        answer.setAnswerer(user);
        answer.setAnswerOption(option);

        String answerAsJson = mapper.toJson(answer);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(answerAsJson))
                .andExpect(status().isBadRequest());
    }

    private Question saveQuestion(User user) {
        Question question = new Question();
        question.setCreator(user);
        return questionRepository.save(question);
    }
}
