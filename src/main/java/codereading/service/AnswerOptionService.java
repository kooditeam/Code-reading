
package codereading.service;

import codereading.domain.AnswerOption;
import java.util.List;

import codereading.repository.AnswerOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionService {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    boolean answerOptionsValid(List<AnswerOption> options) {
        if (options == null || options.size() < 2) {
            return false;
        }
        for (AnswerOption option : options) {
            if (option.getAnswerText() == null || option.getAnswerText().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    boolean answerOptionVExists(AnswerOption option) {
        return answerOptionRepository.getOne(option.getId()) == null;
    }
    
}
