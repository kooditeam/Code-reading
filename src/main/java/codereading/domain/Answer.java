
package codereading.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import codereading.serializing.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer is a single answer from a user to a question. Contains the info
 * on what the answer option was for this answer.
 */
@Entity
public class Answer extends AbstractPersistable<Long> {

    @ManyToOne
    private AnswerOption answerOption;

    @JsonSerialize(using = UserSerializer.class)
    @ManyToOne
    private User answerer;

    public User getAnswerer() {
        return answerer;
    }

    public void setAnswerer(User answerer) {
        this.answerer = answerer;
    }

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }
}
