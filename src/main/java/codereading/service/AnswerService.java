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

    public Feedback saveAnswer(Answer answer) {

        answerRepository.save(answer);

        AnswerOption option = answer.getAnswerOption();
        option = answerOptionRepository.findOne(option.getId());

        User user = userRepository.findOne(answer.getAnswerer().getId());
        userRepository.save(user);

        return new Feedback(option.getIsCorrect(),
                option.getExplanation());
    }
}
