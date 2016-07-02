
package codereading.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer option belongs to question, which can have multiple answer options.
 * Each answer option has text to tell the user what the option is, and info
 * whether the answer option is the correct one to the question or not. 
 */
@Entity
public class AnswerOption extends AbstractPersistable<Long> {

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
