
package codereading.controller;

import codereading.domain.Question;
import codereading.repository.QuestionRepository;
import codereading.service.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionService questionService;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Question> questions() {
        return questionRepository.findAll();
    }

    @RequestMapping(value= "/new", method = RequestMethod.POST)
    public Question createQuestion(@RequestBody Question question) {
        return questionRepository.save(question);
    }
    
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.PUT)
    public Question updateQuestion(@RequestBody Question question,
            @PathVariable Long id) {

        return questionService.update(question, id);
    }
    
}
