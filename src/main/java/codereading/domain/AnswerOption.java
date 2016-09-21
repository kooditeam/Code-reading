
package codereading.domain;

import codereading.serializing.QuestionSerializer;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer option belongs to question, which can have multiple answer options.
 * The explanation field can be used to tell why the option was wrong or right.
 */
@Entity
public class AnswerOption extends AbstractPersistable<Long> {

    @JsonSerialize(using = QuestionSerializer.class)
    @ManyToOne
    private Question question;

    private String answerText;
    private Boolean isCorrect;

    private String explanation;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
