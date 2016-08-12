
package codereading.domain;

import codereading.Main;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.QuestionRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class AnswerOptionTest {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuestionRepository questionRepository;

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
    public void serializingAnswerOptionItsQuestionIsSerializedOnlyAsId() throws IOException {
        Question question = saveQuestion();
        AnswerOption option = saveAnswerOptionWithQuestion(question);

        assertTrue(json(option).contains("\"question\":" + question.getId()));
    }

    @Test
    public void serializingQuestionItsAnswerOptionsSerializeTheQuestionFieldOnlyAsId() throws IOException {
        Question question = saveQuestion();

        AnswerOption option1 = saveAnswerOptionWithQuestion(question);
        AnswerOption option2 = saveAnswerOptionWithQuestion(question);

        List<AnswerOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        question.setAnswerOptions(options);
        question = questionRepository.save(question);

        assertTrue(json(question).contains("\"id\":" + option1.getId() + ",\"question\":" + question.getId()));
        assertTrue(json(question).contains("\"id\":" + option2.getId() + ",\"question\":" + question.getId()));
    }

    private Question saveQuestion() {
        return questionRepository.save(new Question());
    }

    private AnswerOption saveAnswerOptionWithQuestion(Question question) {
        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        return answerOptionRepository.save(option);
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }
}
