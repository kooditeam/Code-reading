package codereading.controller;

import codereading.domain.Answer;
import codereading.domain.Feedback;
import codereading.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(value = "/answers/new", method = RequestMethod.POST)
    public ResponseEntity<Feedback> checkAnswer(@RequestBody Answer answer) {

        if (!answerService.answerValid(answer)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Feedback feedback = answerService.saveAnswer(answer);

        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }
}
