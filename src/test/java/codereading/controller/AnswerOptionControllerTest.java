
package codereading.controller;

import codereading.Main;
import codereading.domain.AnswerOption;
import codereading.repository.AnswerOptionRepository;

import com.google.gson.Gson;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MvcResult;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class AnswerOptionControllerTest {
    
    private final String API_URI = "/answeroptions";
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    private MockMvc mockMvc;
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    private Gson mapper;
    
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
    public void postRequestToCreateNewQuestionSavesItToTheDatabase() throws Exception {
        AnswerOption answerOption = new AnswerOption();
        answerOption.setAnswerText("answerText_test1");
        answerOption.setIsCorrect(false);
        
        String questionJson = json(answerOption);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(questionJson))
                .andExpect(status().isOk());
        
        assertTrue(answerOptionRepository.count() == 1);
    }
    
    @Test
    public void whenGettingAnswerOptionsTheirIsCorrectInfoIsNotAccessible() throws Exception {
        AnswerOption option1 = createAndSaveAnswerOption(2, false);
        AnswerOption option2 = createAndSaveAnswerOption(3, true);
        AnswerOption option3 = createAndSaveAnswerOption(4, true);       

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andReturn();
        
        String result = res.getResponse().getContentAsString();
        assertFalse(result.contains("isCorrect"));

        AnswerOption[] options = mapper.fromJson(result, AnswerOption[].class);
        
        for (AnswerOption option : options) {
            assertFalse(option.getIsCorrect());
        }
        
        assertFalse(option1.getIsCorrect());
        assertTrue(option2.getIsCorrect());
        assertTrue(option3.getIsCorrect());
                
    }
    
    private AnswerOption createAndSaveAnswerOption(int differentiator, boolean isCorrect) {
        AnswerOption answerOption = new AnswerOption();
        answerOption.setAnswerText("answerText_test" + differentiator);
        answerOption.setIsCorrect(isCorrect);
        
        return answerOptionRepository.save(answerOption);
    }
    
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        
        return mockHttpOutputMessage.getBodyAsString();
    }


}
