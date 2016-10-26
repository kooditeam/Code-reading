package codereading.service;

import codereading.domain.Answer;
import codereading.domain.AnswerOption;
import codereading.domain.Feedback;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.AnswerRepository;
import codereading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private AnswerOptionService answerOptionService;

    @Autowired
    private UserService userService;

    public Feedback saveAnswer(Answer answer) {
        userService.getExistingOrNewUser(answer.getAnswerer().getStudentNumber());

        answerRepository.save(answer);

        AnswerOption option = answer.getAnswerOption();
        option = answerOptionRepository.findOne(option.getId());

        return new Feedback(option.getIsCorrect(),
                option.getExplanation());
    }

    public boolean answerValid(Answer answer) {
        if (answer == null || answer.getAnswerer() == null || answer.getAnswerOption() == null
                || answer.getAnswerOption().getId() == null) {
            return false;
        }

        if (studentNumberNotValid(answer.getAnswerer().getStudentNumber())) {
            return false;
        }
        if (answerOptionService.answerOptionVExists(answer.getAnswerOption())) {
            return false;
        }

        return true;
    }

    private boolean studentNumberNotValid(String studentNumber) {
        return studentNumber == null || studentNumber.isEmpty();
    }
}
