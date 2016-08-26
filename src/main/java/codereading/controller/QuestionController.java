
package codereading.controller;

import codereading.domain.Question;
import codereading.repository.QuestionRepository;
import codereading.service.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to update questions.
 */
@RestController
@RequestMapping("/questions")
public class QuestionController {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionService questionService;
    
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.PUT)
    public Question updateQuestion(@RequestBody Question question,
            @PathVariable Long id) {

        return questionService.update(question, id);
    }
    
}
