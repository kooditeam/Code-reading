
package codereading.service;

import codereading.domain.Question;
import codereading.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public Question update(Question updated, Long id) {
        Question toBeUpdated = questionRepository.findOne(id);
        toBeUpdated.setCode(updated.getCode());
        toBeUpdated.setInfo(updated.getInfo());
        toBeUpdated.setTitle(updated.getTitle());
        
        return toBeUpdated;
    }
}
