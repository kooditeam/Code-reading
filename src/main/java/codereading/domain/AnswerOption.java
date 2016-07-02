
package codereading.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer option belongs to question, which can have multiple answer options.
 * Each answer option has text to tell the user what the option is, and info
 * whether the answer option is the correct one to the question or not. When
 * an answer option is serialized to JSON, its question field's info is 
 * serialized only as its id.
 */
@Entity
public class AnswerOption extends AbstractPersistable<Long> {

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne
    private Question question;

    private String answerText;
    private boolean isCorrect;

    public Question getQuestion() {
        return question;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

}
