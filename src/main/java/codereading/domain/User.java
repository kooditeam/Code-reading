
package codereading.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * User is a student having a student number. User can answer questions and 
 * create new ones.
 */
@Entity
public class User extends AbstractPersistable<Long> {

    @NotNull
    @NotBlank
    private String studentNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> createdQuestions;

    public User() {}

    public User(String studentNumber) {
        this.studentNumber = studentNumber;
    }

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

    public void addAnswer(Answer answer) {
        if (answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answer);
    }
}
