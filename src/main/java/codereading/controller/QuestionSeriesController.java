
package codereading.controller;

import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.service.QuestionSeriesService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionseries")
public class QuestionSeriesController {

    @Autowired
    private QuestionSeriesService questionSeriesService;

    @RequestMapping(method = RequestMethod.GET)
    public List<QuestionSeries> allQuestionSeries() {
        return questionSeriesService.findAll();
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public QuestionSeries createQuestionSeries(@RequestBody QuestionSeries series) {
        return questionSeriesService.save(series);
    }

    @RequestMapping(value = "/{id}/questions/new", method = RequestMethod.POST)
    public QuestionSeries createQuestionForSeries(@PathVariable(value = "id") Long seriesId,
            @RequestBody Question question) {

        return questionSeriesService.createQuestionToSeries(seriesId, question);
    }

}
