
package codereading.controller;

import codereading.Main;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.SeriesUserAndQuestionWrapper;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.transaction.Transactional;
import org.junit.Before;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class QuestionSeriesControllerTest {

    private final String API_URI = "/questionseries";

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionSeriesRepository questionSeriesRepository;

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
    }

    @Test
    public void postRequestToCreateNewQuestionSeriesSavesItToTheDatabase() throws Exception {     
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle1");
        SeriesUserAndQuestionWrapper wrapper = new SeriesUserAndQuestionWrapper();
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233255");

        String wrapperJson = json(wrapper);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk());

        assertTrue(questionSeriesRepository.count() == 1);
    }

    @Test
    public void postRequestToCreateNewQuestionForASeriesSavesTheQuestionToDatabaseAndToTheSeries() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle1");
        series = questionSeriesRepository.save(series);

        Question question = new Question();
        question.setTitle("testTitle1");
        question.setCode("testCode1");
        question.setInfo("testInfo1");

        SeriesUserAndQuestionWrapper wrapper = new SeriesUserAndQuestionWrapper();
        wrapper.setQuestion(question);
        wrapper.setStudentNumber("10323133");
        String questionJson = json(wrapper);

        this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/new")
                .contentType(contentType)
                .content(questionJson))
                .andExpect(status().isOk());

        assertTrue(questionRepository.count() == 1);
        assertTrue(series.getQuestions().size() == 1);
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        
        return mockHttpOutputMessage.getBodyAsString();
    }
}