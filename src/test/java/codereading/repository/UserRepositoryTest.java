
package codereading.repository;

import codereading.Main;
import codereading.domain.User;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void tryingToFindIdByStudentNumberReturnsNullIfThereIsNoSuchUser() {
        Long id = userRepository.idOfUserByStudentNumber("test");
        assertNull(id);
    }

    @Test
    public void tryingToFindIdOfStudentNumberReturnsMatchWithExistingNumberWhenOnlyOneUserExists() {
        User user = saveUser("010101011");

        Long id = userRepository.idOfUserByStudentNumber("010101011");
        assertEquals(user.getId(), id);
    }

    @Test
    public void tryingToFindIdOfStudentNumberReturnsMatchWithExistingNumberWhenMultipleUsersExist() {
        User user1 = saveUser("020202222");
        User user2 = saveUser("020222222");
        User user3 = saveUser("020202022");
        User user4 = saveUser("022222222");

        Long id = userRepository.idOfUserByStudentNumber("020202022");
        assertEquals(user3.getId(), id);

    }

    private User saveUser(String studentNumber)  {
        User user = new User();
        user.setStudentNumber(studentNumber);
        return userRepository.save(user);
    }

}
