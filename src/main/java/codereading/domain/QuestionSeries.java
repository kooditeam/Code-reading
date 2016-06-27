
package codereading.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * QuestionsSeries contains multiple questions. Each series has its own title.
 */
@Entity
public class QuestionSeries extends AbstractPersistable<Long> {
    
    @OneToMany
    private List<Question> questions;
    
    private String title;

    public List<Question> getQuestions() { 
        return questions;
    }

    public String getTitle() {
        return title;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
    }
    
}
