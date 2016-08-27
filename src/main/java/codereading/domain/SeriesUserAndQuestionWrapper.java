package codereading.domain;

/**
 * Wrapper just to make controller slightly easier.
 */
public class SeriesUserAndQuestionWrapper {

    private QuestionSeries questionSeries;
    private String studentNumber;
    private Question question;

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
}
