
package codereading.domain;

import javax.persistence.Entity;
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

    public User() {}

    public User(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
