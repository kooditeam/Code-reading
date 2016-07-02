
package codereading.repository;

import codereading.Main;
import codereading.domain.Answer;
import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.User;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@Transactional
public class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Test
    public void whenNoQuestionsInTheDatabaseThereAreNoUnansweredQuestionsForAUser() {
        User user = saveUser();

        assertTrue(questionRepository.count() == 0);
        assertTrue(questionRepository.questionsNotAnsweredByUser(user.getId()).isEmpty());
    }

    @Test
    public void withOneQuestionInTheDatabaseAndUserNotHavingAnsweredAnyReturnsThatOneQuestion() {
        User user = saveUser();
        Question question = saveQuestion();
        saveAnswer(question, user);

        assertTrue(questionRepository.questionsNotAnsweredByUser(user.getId()).isEmpty());
    }

    @Test
    public void withMultipleQuestionsInTheDatabaseAllOfThemAreUnansweredForUserAtStart() {
        User user = saveUser();
        saveMultipleQuestions(5);

        assertTrue(questionRepository.count() == 5);
        assertTrue(questionRepository.questionsNotAnsweredByUser(user.getId()).size() == 5);
    }

    @Test
    public void withMultipleQuestionsInTheDatabaseAndUserHavingAnsweredOnlyOneReturnsOnlyThatOneAnsweredQuestion() {
        User user = saveUser();
        Question question = saveMultipleQuestions(3).get(0);

        Answer answer = saveAnswer(question, user);
        answerRepository.save(answer);

        assertTrue(questionRepository.count() == 3);
        assertTrue(questionRepository.questionsNotAnsweredByUser(user.getId()).size() == 2);
    }

    @Test
    public void withAllQuestionsAnsweredByAUserTheyDoNotHaveAnyUnansweredQuestions() {
        User user = saveUser();
        answerQuestions(saveMultipleQuestions(4), user);

        assertTrue(questionRepository.count() == 4);
        assertTrue(questionRepository.questionsNotAnsweredByUser(user.getId()).isEmpty());

    }

    public void answeredQuestionsByAnotherUserAreNotCountedAsAnsweredQuestionsForSelf() {
        User user1 = saveUser();
        User user2 = saveUser();

        answerQuestions(saveMultipleQuestions(2), user1);

        assertTrue(questionRepository.count() == 2);
        assertTrue(questionRepository.questionsNotAnsweredByUser(user1.getId()).isEmpty());
        assertTrue(questionRepository.questionsNotAnsweredByUser(user2.getId()).size() == 2);
    }

    private void answerQuestions(List<Question> questions, User user) {
        for (Question question : questions) {
            saveAnswer(question, user);
        }
    }

    private List<Question> saveMultipleQuestions(int amount) {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            questions.add(saveQuestion());
        }
        return questions;
    }

    private Question saveQuestion() {
        Question question = new Question();
        return questionRepository.save(question);
    }

    private User saveUser() {
        User user = new User();
        return userRepository.save(user);
    }

    private Answer saveAnswer(Question question, User user) {
        AnswerOption option = saveAnswerOption(question);

        Answer answer = new Answer();
        answer.setAnswerOption(option);
        answer.setAnswerer(user);

        return answerRepository.save(answer);
    }

    private AnswerOption saveAnswerOption(Question question) {
        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        return answerOptionRepository.save(option);
    }
}
