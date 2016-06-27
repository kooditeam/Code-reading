
package codereading.domain;

import java.util.List;
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
 */
@Entity
public class Question extends AbstractPersistable<Long> {

    private String title;
    boolean removed;
    
    @Column(columnDefinition="varchar(10000)")
    private String info;

    @Column(columnDefinition="varchar(10000)")
    private String code;
    
    @OneToMany
    private List<AnswerOption> answerOptions;

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
