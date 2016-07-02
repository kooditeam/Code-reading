
package codereading.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * User is a student having a student number. User can answer questions and 
 * create new ones.
 */
@Entity
public class User extends AbstractPersistable<Long> {

    private String studentNumber;

    @OneToMany
    private List<Answer> answers;

    @OneToMany
    private List<Question> createdQuestions;

    public List<Question> getCreatedQuestions() {
        return createdQuestions;
    }

    public void setCreatedQuestions(List<Question> createdQuestions) {
        this.createdQuestions = createdQuestions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
