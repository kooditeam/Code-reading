
package codereading.service;

import codereading.domain.AnswerOption;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionService {

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
    
}
