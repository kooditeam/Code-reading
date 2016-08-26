
package codereading.repository;

import codereading.domain.Question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    /*
     * Fetches all questions that have not been answered correctly by a given 
     * user identified by their id.
     */
    @Query("SELECT q FROM Question q WHERE q.id NOT IN "
            + "(SELECT a.answerOption.question.id FROM Answer a WHERE a.answerer.id = :userId "
            + "and a.answerOption.isCorrect != false)")
    List<Question> questionsNotAnsweredCorrectly(@Param("userId") Long userId);

}
