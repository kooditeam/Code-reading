
package codereading.domain;

import codereading.Main;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import codereading.repository.UserRepository;
import org.junit.Test;
import static org.junit.Assert.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class QuestionTest {

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    public void whenSerializingQuestionItsQuestionSeriesIsSerializedOnlyAsId() throws IOException {
        QuestionSeries series = saveQuestionSeries();
        Question question = saveQuestionAlongWithSeriesAndUser(series, new User("98982222"));

        assertTrue(json(question).contains("\"questionSeries\":" + series.getId()));
    }

    @Test
    public void whenSerializingQuestionSeriesItsQuestionsSerializeTheQuestionSeriesFieldOnlyAsId() throws IOException {
        QuestionSeries series = saveQuestionSeries();

        Question question1 = saveQuestionAlongWithSeriesAndUser(series, new User("98982223"));
        Question question2 = saveQuestionAlongWithSeriesAndUser(series, new User("98982224"));

        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);

        series.setQuestions(questions);
        series = questionSeriesRepository.save(series);

        assertTrue(json(series).contains("\"questionSeries\":" + series.getId()));
    }

    @Test
    public void whenSerializingQuestionSeriesItsQuestionsSerializeTheCreatorFieldOnlyAsId() throws IOException {
        QuestionSeries series = saveQuestionSeries();

        User user = new User("333444551");
        Question question1 = saveQuestionAlongWithSeriesAndUser(series, user);
        Question question2 = saveQuestionAlongWithSeriesAndUser(series, user);

        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);

        series.setQuestions(questions);
        series = questionSeriesRepository.save(series);

        assertTrue(json(series).contains("\"creator\":{\"id\":" + user.getId() + "}"));
    }

    private QuestionSeries saveQuestionSeries() {
        QuestionSeries series = new QuestionSeries();
        return questionSeriesRepository.save(series);
    }

    private Question saveQuestionAlongWithSeriesAndUser(QuestionSeries series, User user) {
        user = userRepository.save(user);
        Question question = new Question();
        question.setQuestionSeries(series);
        question.setCreator(user);
        return questionRepository.save(question);
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }
}
