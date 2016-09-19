
package codereading.domain;

import codereading.serializing.AnswerOptionSerializer;
import codereading.serializing.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Questions are single exercises.
 * When serializing a question to JSON, its questionSeries and creator fields are
 * serialized only as their id.
 */
@Entity
public class Question extends AbstractPersistable<Long> {

    private String title;

    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private User creator;

    @Column(columnDefinition="varchar(10000)")
    private String info;

    @Column(columnDefinition="varchar(10000)")
    private String code;

    @JsonSerialize(using = AnswerOptionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL)
    private List<AnswerOption> answerOptions;

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private QuestionSeries questionSeries;

    @JsonSerialize(using = UserSerializer.class)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

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

    @JsonIgnore
    @Override
    public boolean isNew() {
        return false;
    }

}
