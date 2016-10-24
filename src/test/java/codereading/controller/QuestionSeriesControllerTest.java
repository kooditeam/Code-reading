
package codereading.controller;

import codereading.Main;
import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.SeriesRequestWrapper;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.QuestionSeriesRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import codereading.repository.UserRepository;
import com.google.gson.Gson;
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

import org.springframework.test.web.servlet.MvcResult;
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
    private UserRepository userRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private WebApplicationContext webAppContext;

    private Gson mapper;

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
    public void postRequestToCreateNewQuestionSeriesWithoutAQuestionSavesTheSeriesToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle1");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233255");

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle1", response.getQuestionSeries().getTitle());
        assertEquals("012233255", response.getStudentNumber());
        assertNull(response.getQuestion());
        assertNull(response.getAnswerOptions());
    }

    @Test
    public void postRequestToCreateANewSeriesDoesNotSaveTheSeriesToDatabaseIfTheSeriesIsNull() throws Exception {
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestionSeries(null);
        wrapper.setStudentNumber("012233256");

        String wrapperJson = json(wrapper);

        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);
        assertNull(response);
    }

    @Test
    public void postRequestToCreateNewSeriesWithoutQuestionDoesNotSaveAnyQuestionsToDB() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle3");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233257");

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle3", response.getQuestionSeries().getTitle());
        assertEquals("012233257", response.getStudentNumber());
        assertNull(response.getQuestion());
        assertNull(response.getAnswerOptions());
    }

    @Test
    public void postRequestToCreateNewSeriesWithAQuestionSavesBothToTheDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle4");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();

        Question question = new Question();
        question.setTitle("questionTitle");
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        wrapper.setQuestion(question);
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233258");
        wrapper.setAnswerOptions(answerOptions);

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 1);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle4", response.getQuestionSeries().getTitle());
        assertEquals("012233258", response.getStudentNumber());
        assertEquals("questionTitle", response.getQuestion().getTitle());
        assertTrue(response.getAnswerOptions().size() == 3);
    }

    @Test
    public void postRequestToCreateNewSeriesWithNullUsernameDoesNotSaveAnythingToDB() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle5");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber(null);

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);
        assertNull(response);
    }

    @Test
    public void postRequestToCreateANewSeriesWithEmptyUsernameDoesNotSaveAnythingToDB() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle6");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("");

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);
        assertNull(response);
    }

    @Test
    public void creatingNewSeriesWithQuestionSavesTheSeriesToTheQuestion() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle7");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233259");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        assertNotNull(question.getQuestionSeries());
        assertEquals(series.getTitle(), question.getQuestionSeries().getTitle());

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        series = questionSeriesRepository.findAll().get(0);
        assertEquals("seriesTitle7", response.getQuestionSeries().getTitle());
        assertEquals("012233259", response.getStudentNumber());
        assertEquals(series.getId(), response.getQuestion().getQuestionSeries().getId());
        assertTrue(response.getAnswerOptions().size() == 2);
    }

    @Test
    public void creatingNewSeriesWithQuestionSavesTheCreatorInfoToUser() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle8");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233210");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionRepository.count() == 1);
        assertTrue(userRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        assertNotNull(question.getCreator());
        assertEquals(user.getStudentNumber(), question.getCreator().getStudentNumber());

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle8", response.getQuestionSeries().getTitle());
        assertEquals("012233210", response.getStudentNumber());
        assertEquals(user.getId(), response.getQuestion().getCreator().getId());
        assertTrue(response.getAnswerOptions().size() == 2);
    }

    @Test
    public void postRequestToCreateNewSeriesWithAQuestionAndAnswerOptionsSavesTheAnswerOptionsToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle5");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();

        Question question = new Question();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(3);

        wrapper.setQuestion(question);
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233259");
        wrapper.setAnswerOptions(answerOptions);

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(answerOptionRepository.count() == 3);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertNotNull(response.getQuestionSeries());
        assertNotNull(response.getStudentNumber());
        assertNotNull(response.getQuestion());
        assertTrue(response.getAnswerOptions().size() == 3);
    }

    @Test
    public void postRequestToCreateNewSeriesWithAQuestionAndAnswerOptionsSavesTheAnswerOptionsToDatabaseWithTheQuestionInfo() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle5");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();

        Question question = new Question();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(2);

        wrapper.setQuestion(question);
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233259");
        wrapper.setAnswerOptions(answerOptions);

        String wrapperJson = json(wrapper);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk());

        assertTrue(questionRepository.count() == 1);
        assertTrue(answerOptionRepository.count() == 2);

        question = questionRepository.findAll().get(0);
        List<AnswerOption> options = answerOptionRepository.findAll();
        for (AnswerOption option : options) {
            assertTrue(option.getQuestion().getId() == question.getId());
        }
    }

    @Test
    public void postRequestToCreateNewSeriesWithAQuestionWithNotEnoughAnswerOptionsSavesOnlyTheSeriesToTheDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle5");
        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();

        Question question = new Question();
        List<AnswerOption> answerOptions = createAnswerOptionsForQuestion(1);

        wrapper.setQuestion(question);
        wrapper.setQuestionSeries(series);
        wrapper.setStudentNumber("012233259");
        wrapper.setAnswerOptions(answerOptions);

        String wrapperJson = json(wrapper);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk());

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(userRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);
        assertTrue(answerOptionRepository.count() == 0);
    }

    @Test
    public void addingQuestionToExistingSeriesSavesTheQuestionToTheDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle9");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233211");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionRepository.count() == 1);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle9", response.getQuestionSeries().getTitle());
        assertEquals("012233211", response.getStudentNumber());
        assertNotNull(response.getQuestion());
        assertTrue(response.getAnswerOptions().size() == 2);
    }

    @Test
    public void tryingToAddNewQuestionToSeriesWhenThePostedQuestionIsNullDoesNotSaveAnythingToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle9");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(null);
        wrapper.setStudentNumber("012233212");

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 0);
        assertTrue(userRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);
        assertNull(response);
    }

    @Test
    public void tryingToAddNewQuestionToSeriesWhenThePostedSeriesIdIsNotInDatabaseDoesNotSaveAnythingToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle9");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233212");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + 100 + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 0);
        assertTrue(userRepository.count() == 0);
        assertTrue(answerOptionRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);
        assertNull(response);
    }

    @Test
    public void addingQuestionToSeriesSavesTheSeriesToTheQuestion() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle10");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233213");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(questionRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        assertNotNull(question.getQuestionSeries());
        assertEquals(series.getTitle(), question.getQuestionSeries().getTitle());

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        series = questionSeriesRepository.findAll().get(0);
        assertEquals("seriesTitle10", response.getQuestionSeries().getTitle());
        assertEquals("012233213", response.getStudentNumber());
        assertNotNull(response.getQuestion());
        assertEquals(series.getId(), response.getQuestion().getQuestionSeries().getId());
        assertTrue(response.getAnswerOptions().size() == 2);
    }

    @Test
    public void addingQuestionToSeriesSavesTheCreatorToTheQuestion() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle11");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233214");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionRepository.count() == 1);
        assertTrue(userRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        assertNotNull(question.getCreator());
        assertEquals(user.getStudentNumber(), question.getCreator().getStudentNumber());

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertEquals("seriesTitle11", response.getQuestionSeries().getTitle());
        assertEquals("012233214", response.getStudentNumber());
        assertNotNull(response.getQuestion());
        assertEquals(user.getId(), response.getQuestion().getCreator().getId());
        assertTrue(response.getAnswerOptions().size() == 2);
    }

    @Test
    public void addingQuestionToSeriesSavesTheQuestionsAnswerOptionsToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle11");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233214");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk());

        assertTrue(answerOptionRepository.count() == 2);
    }

    @Test
    public void addingQuestionToSeriesSavesTheQuestionsAnswerOptionsWithTheQuestionInfoToDatabase() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle11");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233214");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(2));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(answerOptionRepository.count() == 2);
        assertTrue(questionRepository.count() == 1);

        Question question = questionRepository.findAll().get(0);
        List<AnswerOption> options = answerOptionRepository.findAll();

        for (AnswerOption option : options) {
            assertNotNull(option.getQuestion());
            assertTrue(option.getQuestion().getId() == question.getId());
        }

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertTrue(response.getAnswerOptions().size() == 2);
        for (AnswerOption option : response.getAnswerOptions()) {
            assertEquals(question.getId(), option.getQuestion().getId());
        }
    }

    @Test
    public void addingQuestionToSeriesDoesNotSaveAnythingToDatabaseIfThereAreNotEnoughAnswerOptions() throws Exception {
        QuestionSeries series = new QuestionSeries();
        series.setTitle("seriesTitle11");
        questionSeriesRepository.save(series);

        SeriesRequestWrapper wrapper = new SeriesRequestWrapper();
        wrapper.setQuestion(new Question());
        wrapper.setStudentNumber("012233214");
        wrapper.setAnswerOptions(createAnswerOptionsForQuestion(1));

        String wrapperJson = json(wrapper);
        MvcResult res = this.mockMvc.perform(post(API_URI + "/" + series.getId() + "/questions/" + "/new")
                .contentType(contentType)
                .content(wrapperJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(questionSeriesRepository.count() == 1);
        assertTrue(answerOptionRepository.count() == 0);
        assertTrue(questionRepository.count() == 0);
        assertTrue(userRepository.count() == 0);

        SeriesRequestWrapper response = mapper.fromJson(res.getResponse().getContentAsString(), SeriesRequestWrapper.class);

        assertNull(response);
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        
        return mockHttpOutputMessage.getBodyAsString();
    }

    private List<AnswerOption> createAnswerOptionsForQuestion(int amountOfOptions) {
        List<AnswerOption> options = new ArrayList<>();
        for (int i = 1; i <= amountOfOptions; i++) {
            AnswerOption option = new AnswerOption();
            option.setAnswerText("test answer test");
            options.add(option);
        }

        return options;
    }
}