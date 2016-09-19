package codereading.service;

import codereading.domain.User;
import codereading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    User getOrCreateUser(String studentNumber) {
        if (studentNumber == null || studentNumber.isEmpty()) {
            return null;
        }

        User user = userRepository.findByStudentNumber(studentNumber);
        if (user == null) {
            user = userRepository.save(new User(studentNumber));
        }
        return user;
    }
}
