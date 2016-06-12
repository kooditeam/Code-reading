package codereading.controller;

import codereading.domain.AnswerOption;
import codereading.repository.AnswerOptionRepository;
import codereading.service.AnswerOptionService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * At least for now this controller is primarily to make testing simpler.
 */
@RequestMapping("/answeroptions")
@RestController
public class AnswerOptionController {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @Autowired
    private AnswerOptionService answerOptionService;

    @RequestMapping(method = RequestMethod.GET)
    public String answerOptions() throws JsonProcessingException {
        return answerOptionService.getAnswerOptionsWithoutInfoOnWhichIsCorrect();
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public AnswerOption createAnswerOption(@RequestBody AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

}
