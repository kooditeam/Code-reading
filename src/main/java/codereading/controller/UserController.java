
package codereading.controller;

import codereading.domain.Question;
import codereading.repository.QuestionRepository;
import codereading.repository.UserRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Returns all questions that are not answered or have been answered
     * incorrectly by a given user. If the user does not exist, then all
     * questions are returned.
     * @param studentNumber
     * @return questions not answered correctly.
     */
    @RequestMapping(value = "/{studentNumber}/unanswered", method = RequestMethod.GET)
    public List<Question> getQuestions(@PathVariable String studentNumber) {
        Long userId = userRepository.idOfUserByStudentNumber(studentNumber);

        if (userId == null) {
            return questionRepository.findAll();
        }

        return questionRepository.questionsNotAnsweredCorrectly(userId);
    }

}
