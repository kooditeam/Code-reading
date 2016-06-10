
package codereading.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer is a single answer from a user to a question. Contains the info
 * on what the answer option was for this answer.
 */
@Entity
public class Answer extends AbstractPersistable<Long> {
    
    @ManyToOne
    private AnswerOption answerOption;

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }
 
    
}
