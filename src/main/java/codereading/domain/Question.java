
package codereading.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Questions are single exercises, which each contain info on the question
 * (for the user to know what it's about), title and a piece of code for users
 * to try to understand. Each question can have multiple answer options and
 * belongs to a single question series.
 * Questions can be marked as removed instead of permanently deleting them.
 * When serializing a question to JSON, its questionSeries field is serialized
 * only as its id.
 */
@Entity
public class Question extends AbstractPersistable<Long> {

    private String title;
    boolean removed;

    @Column(columnDefinition="varchar(10000)")
    private String info;

    @Column(columnDefinition="varchar(10000)")
    private String code;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AnswerOption> answerOptions;

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne
    private QuestionSeries questionSeries;

    public QuestionSeries getQuestionSeries() {
        return questionSeries;
    }

    public void setQuestionSeries(QuestionSeries questionSeries) {
        this.questionSeries = questionSeries;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
