
package codereading.repository;

import codereading.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT id FROM User u where u.studentNumber = :studentNumber")
    Long idOfUserByStudentNumber(@Param("studentNumber") String studentNumber);
}
