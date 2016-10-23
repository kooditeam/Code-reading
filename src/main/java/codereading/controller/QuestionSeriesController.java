
package codereading.controller;

import codereading.domain.QuestionSeries;
import codereading.domain.SeriesRequestWrapper;
import codereading.service.QuestionSeriesService;

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

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SeriesRequestWrapper createNewQuestionSeries(
            @RequestBody SeriesRequestWrapper newSeriesWrapper) {

        return questionSeriesService.createNewSeries(newSeriesWrapper.getQuestionSeries(),
                newSeriesWrapper.getQuestion(),
                newSeriesWrapper.getStudentNumber(),
                newSeriesWrapper.getAnswerOptions());
    }

    @RequestMapping(value = "/{id}/questions/new", method = RequestMethod.POST)
    public SeriesRequestWrapper createQuestionForSeries(@PathVariable(value = "id") Long seriesId,
            @RequestBody SeriesRequestWrapper questionUserAndOptionsWrapper) {

        return questionSeriesService.createQuestionToSeries(seriesId,
                questionUserAndOptionsWrapper.getQuestion(),
                questionUserAndOptionsWrapper.getStudentNumber(),
                questionUserAndOptionsWrapper.getAnswerOptions());
    }

}
