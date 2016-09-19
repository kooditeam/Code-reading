
package codereading.controller;

import codereading.domain.QuestionSeries;
import codereading.domain.SeriesUserAndQuestionWrapper;
import codereading.service.QuestionSeriesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to create question series and add questions to them.
 */
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
    public QuestionSeries createQuestionSeries(
            @RequestBody SeriesUserAndQuestionWrapper seriesAndUserWrapper) {

        return questionSeriesService.save(seriesAndUserWrapper.getQuestionSeries(),
                seriesAndUserWrapper.getStudentNumber());
    }

    @RequestMapping(value = "/{id}/questions/new", method = RequestMethod.POST)
    public QuestionSeries createQuestionForSeries(@PathVariable(value = "id") Long seriesId,
            @RequestBody SeriesUserAndQuestionWrapper questionAndUserWrapper) {

        return questionSeriesService.createQuestionToSeries(seriesId,
                questionAndUserWrapper.getQuestion(),
                questionAndUserWrapper.getStudentNumber());
    }

}
