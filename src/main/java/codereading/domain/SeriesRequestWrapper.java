package codereading.domain;

import java.util.List;

/**
 * Wrapper for handling creating new question series and adding new
 * questions for them.
 */
public class SeriesRequestWrapper {

    private QuestionSeries questionSeries;
    private String studentNumber;
    private Question question;
    private List<AnswerOption> answerOptions;

    public SeriesRequestWrapper() {}

    public SeriesRequestWrapper(QuestionSeries series, String studentNumber,
                                Question question, List<AnswerOption> answerOptions) {
        this.questionSeries = series;
        this.studentNumber = studentNumber;
        this.question = question;
        this.answerOptions = answerOptions;
    }

    public QuestionSeries getQuestionSeries() {
        return questionSeries;
    }

    public void setQuestionSeries(QuestionSeries questionSeries) {
        this.questionSeries = questionSeries;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }
}
