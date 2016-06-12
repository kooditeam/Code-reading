
package codereading.service;

import codereading.domain.AnswerOption;
import codereading.repository.AnswerOptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewSerializer;
import static com.monitorjbl.json.Match.match;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionService {
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    public String getAnswerOptionsWithoutInfoOnWhichIsCorrect() throws JsonProcessingException {
        List<AnswerOption> options = answerOptionRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonView.class, new JsonViewSerializer());
        mapper.registerModule(module);

        String optionsAsJson = mapper.writeValueAsString(JsonView.with(options).
                onClass(AnswerOption.class, match().exclude("isCorrect")));
        
        return optionsAsJson;
    }
    
}
