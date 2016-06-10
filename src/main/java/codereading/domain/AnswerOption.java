
package codereading.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer option belongs to question, which can have multiple answer options.
 * Each answer option has text to tell the user what the option is, and info
 * whether the answer option is the correct one to the question or not.
 */
@Entity
public class AnswerOption extends AbstractPersistable<Long> {
    
    @OneToMany
    private List<Answer> answers;

    private Question question;

    private String answerText;
    private boolean isCorrect;

    public List<Answer> getAnswers() {
        return answers;
    }

    public Question getQuestion() {
        return question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    
    
}
