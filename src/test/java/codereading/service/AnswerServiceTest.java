package codereading.service;

import codereading.Main;
import codereading.domain.Answer;
import codereading.domain.AnswerOption;
import codereading.domain.Feedback;
import codereading.domain.Question;
import codereading.domain.User;
import codereading.repository.AnswerOptionRepository;
import codereading.repository.AnswerRepository;
import codereading.repository.QuestionRepository;
import codereading.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class AnswerServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerService answerService;

    @Test
    public void answerIsSavedWithUserInfoAndAnswerIsAddedToUsersAnswers() {
        Long answerCOunt = answerRepository.count();
        User user = userRepository.save(new User("055511111"));
        Question question = new Question();
        question.setCreator(user);
        question = questionRepository.save(question);

        AnswerOption answerOption = new AnswerOption();
        answerOption.setIsCorrect(true);
        answerOption.setQuestion(question);
        answerOption = answerOptionRepository.save(answerOption);

        Answer answer = new Answer();
        answer.setAnswerOption(answerOption);
        answer.setAnswerer(user);

        answerService.saveAnswer(answer);

        assertTrue(answerRepository.count() == answerCOunt + 1);

        Answer answerAfter = answerRepository.findAll().get(0);
        assertTrue(answerAfter.getAnswerer().getId() == user.getId());
    }

    @Test
    public void feedbackContainsInfoWhetherOrNotAnswerWasCorrectAlongWithItsExplanation() {
        User user = userRepository.save(new User("055511112"));
        Question question = new Question();
        question.setCreator(user);
        question = questionRepository.save(question);

        AnswerOption answerOption = new AnswerOption();
        answerOption.setIsCorrect(true);
        answerOption.setQuestion(question);
        answerOption.setExplanation("yes, that's just the way it is");
        answerOption = answerOptionRepository.save(answerOption);

        Answer answer = new Answer();
        answer.setAnswerOption(answerOption);
        answer.setAnswerer(user);

        Feedback feedback = answerService.saveAnswer(answer);
        assertNotNull(feedback.getExplanation());
        assertEquals("yes, that's just the way it is", feedback.getExplanation());

        assertNotNull(feedback.getIsCorrect());
        assertTrue(feedback.getIsCorrect());
    }
}