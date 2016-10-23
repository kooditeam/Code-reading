
package codereading.domain;

import codereading.serializing.QuestionSeriesSerializer;
import codereading.serializing.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

    @Column(columnDefinition="varchar(10000)")
    private String info;

    @Column(columnDefinition="varchar(10000)")
    private String code;

    @JsonSerialize(using = UserSerializer.class)
    @NotNull
    @ManyToOne
    private User creator;

    @JsonSerialize(using = QuestionSeriesSerializer.class)
    @ManyToOne
    private QuestionSeries questionSeries;

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
