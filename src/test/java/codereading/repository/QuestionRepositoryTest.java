
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
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
        User user = saveUser(1);

        assertTrue(questionRepository.count() == 0);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).isEmpty());
    }

    @Test
    public void withOneQuestionInTheDatabaseAndUserNotHavingAnsweredAnyReturnsThatOneQuestion() {
        User user = saveUser(2);
        Question question = saveQuestion(10);

        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 1);
    }

    @Test
    public void withOneQuestionInTheDatabaseAndUserHavingAnsweredThatWrongReturnsThatOneAnswer() {
        User user = saveUser(3);
        Question question = saveQuestion(11);
        saveAnswer(question, user, false);

        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 1);
    }

    @Test
    public void withOneQuestionInTheDatabaseAndUserHavingAnsweredThatCorrectlyReturnsEmptyList() {
        User user = saveUser(4);
        Question question = saveQuestion(12);
        saveAnswer(question, user, true);

        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).isEmpty());
    }

    @Test
    public void withMultipleQuestionsInTheDatabaseAllOfThemAreUnansweredForUserAtStart() {
        User user = saveUser(5);
        saveMultipleQuestions(5);

        assertTrue(questionRepository.count() == 5);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 5);
    }

    @Test
    public void withMultipleQuestionsInTheDatabaseAndUserHavingAnsweredOnlyOneAndThatWasCorrectReturnsOnlyThatOneAnsweredQuestion() {
        User user = saveUser(6);
        Question question = saveMultipleQuestions(3).get(0);

        Answer answer = saveAnswer(question, user, true);
        answerRepository.save(answer);

        assertTrue(questionRepository.count() == 3);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 2);
    }

    @Test
    public void withMultipleQuestionsInDatabaseAndUserHavingAnsweredOnlyOneThatWasWrongReturnsEmptyList() {
        User user = saveUser(7);
        Question question = saveMultipleQuestions(3).get(0);

        Answer answer = saveAnswer(question, user, false);
        answerRepository.save(answer);

        assertTrue(questionRepository.count() == 3);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 3);
    }

    @Test
    public void withAllQuestionsAnsweredByAUserCorrectlyTheUserDoesNotHaveAnyUnansweredQuestions() {
        User user = saveUser(8);
        answerQuestionsCorrectly(saveMultipleQuestions(4), user);

        assertTrue(questionRepository.count() == 4);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).isEmpty());
    }

    @Test
    public void withAllQuestionsAnsweredByAUserWrongTheUserHasAllQuestionsStillUnansweredProperly() {
        User user = saveUser(9);
        answerQuestionsWrong(saveMultipleQuestions(4), user);
        
        assertTrue(questionRepository.count() == 4);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 4);
    }

    @Test
    public void someOfTheQuestionsAnsweredCorrectlyAndSomeWrongReturnsAllTheWronglyAnsweredQuestions() {
        User user = saveUser(10);
        answerQuestionsWrong(saveMultipleQuestions(3), user);
        answerQuestionsCorrectly(saveMultipleQuestions(2), user);
        
        assertTrue(questionRepository.count() == 5);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 3);
    }

    @Test
    public void someOfTheQuestionsAnsweredCorrectlyAndSomeWrongAndSomeNotAtAllReturnsAllWronglyAndUnansweredQuestions() {
        User user = saveUser(11);
        answerQuestionsWrong(saveMultipleQuestions(3), user);
        answerQuestionsCorrectly(saveMultipleQuestions(2), user);
        saveMultipleQuestions(3);

        assertTrue(questionRepository.count() == 8);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user.getId()).size() == 6);
    }

    @Test
    public void answeredQuestionsByAnotherUserAreNotCountedAsAnsweredQuestionsForSelf() {
        User user1 = saveUser(12);
        User user2 = saveUser(13);

        answerQuestionsCorrectly(saveMultipleQuestions(2), user1);

        assertTrue(questionRepository.count() == 2);
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user1.getId()).isEmpty());
        assertTrue(questionRepository.questionsNotAnsweredCorrectly(user2.getId()).size() == 2);
    }

    private void answerQuestionsWrong(List<Question> questions, User user) {
        answerQuestions(questions, user, false);
    }

    private void answerQuestionsCorrectly(List<Question> questions, User user) {
        answerQuestions(questions, user, true);
    }

    private void answerQuestions(List<Question> questions, User user, boolean isCorrect) {
        for (Question question : questions) {
            saveAnswer(question, user, isCorrect);
        }
    }

    private List<Question> saveMultipleQuestions(int amount) {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            questions.add(saveQuestion(i));
        }
        return questions;
    }

    private Question saveQuestion(int i) {
        Question question = new Question();
        question.setCreator(new User("sjjsjss" + i));
        return questionRepository.save(question);
    }

    private User saveUser(int i) {
        User user = new User("0112314" + i);
        return userRepository.save(user);
    }

    private Answer saveAnswer(Question question, User user, boolean isCorrect) {
        AnswerOption option = saveAnswerOption(question, isCorrect);

        Answer answer = new Answer();
        answer.setAnswerOption(option);
        answer.setAnswerer(user);

        return answerRepository.save(answer);
    }

    private AnswerOption saveAnswerOption(Question question, boolean isCorrect) {
        AnswerOption option = new AnswerOption();
        option.setIsCorrect(isCorrect);
        option.setQuestion(question);
        return answerOptionRepository.save(option);
    }
}
