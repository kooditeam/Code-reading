
package codereading.controller;

import codereading.Main;
import codereading.domain.Question;
import codereading.repository.QuestionRepository;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.transaction.Transactional;
import static org.junit.Assert.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@Transactional
public class QuestionControllerTest {
    
    private final String API_URI = "/questions";
    
    @Autowired
    private QuestionRepository questionRepository;
    
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
    public void postRequestToCreateNewQuestionSavesItToTheDatabase() throws Exception {     
        Question question = new Question();
        question.setTitle("testTitle1");
        question.setCode("testCode1");
        question.setInfo("testInfo1");
        
        String questionJson = json(question);
        this.mockMvc.perform(post(API_URI + "/new")
                .contentType(contentType)
                .content(questionJson))
                .andExpect(status().isOk());
        
        assertTrue(questionRepository.count() == 1);
    }
    
    @Test
    public void putRequestToUpdateQuestionActuallyUpdatesIt() throws Exception {
        Question question = saveNewQuestion();
        Long id = question.getId();
        
        Question updated = new Question();
        updated.setTitle("updatedTitle");
        updated.setCode("updatedCode");
        updated.setInfo("updatedInfo");
        
        String updatedJson = json(updated);
        this.mockMvc.perform(put(API_URI + "/" + id + "/edit")
                .contentType(contentType)
                .content(updatedJson))
                .andExpect(status().isOk());
        
        Question updatedHopefully = questionRepository.findOne(id);
        
        assertTrue(questionRepository.count() == 1);
        assertEquals("updatedTitle", updatedHopefully.getTitle());
        assertEquals("updatedCode", updatedHopefully.getCode());
        assertEquals("updatedInfo", updatedHopefully.getInfo());
    }
    
    private Question saveNewQuestion() {
        Question question = new Question();
        question.setTitle("testTitle2");
        question.setCode("testCode2");
        question.setInfo("testInfo2");
        
        return questionRepository.save(question);
    }
    
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
