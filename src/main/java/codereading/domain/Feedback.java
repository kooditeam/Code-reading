package codereading.domain;

/**
 * Wrapper to give feedback to user when they answer a question.
 */
public class Feedback {

    private boolean isCorrect;
    private String explanation;

    public Feedback() {}

    public Feedback(Boolean isCorrect, String explanation) {
        if (isCorrect == null) {
            isCorrect = false;
        }
        this.isCorrect = isCorrect;
        this.explanation = explanation;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
