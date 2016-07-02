
package codereading.repository;

import codereading.domain.Question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Fetches all questions that have not been answered by a given user
     * identified by their id.
     * @param userId id of a given user
     * @return Questions not answered by a user
     */
    @Query("SELECT q FROM Question q WHERE q.id NOT IN "
            + "(SELECT a.answerOption.question.id FROM Answer a WHERE a.answerer.id = :userId)")
    List<Question> questionsNotAnsweredByUser(@Param("userId") Long userId);

}
