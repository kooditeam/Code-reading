package codereading.serializing;

import codereading.domain.AnswerOption;
import codereading.domain.SeriesRequestWrapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SeriesRequestWrapperSerializer extends StdSerializer<SeriesRequestWrapper> {

    public SeriesRequestWrapperSerializer() {
        this(null);
    }

    public SeriesRequestWrapperSerializer(Class<SeriesRequestWrapper> s) {
        super(s);
    }

    @Override
    public void serialize(SeriesRequestWrapper wrapper, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        if (wrapper.getQuestionSeries() != null) {
            jgen.writeObject(wrapper.getQuestionSeries());
        }
        if (wrapper.getStudentNumber() != null) {
            jgen.writeStringField("studentNumber", wrapper.getStudentNumber());
        }
        if (wrapper.getQuestion() != null) {
            jgen.writeObject(wrapper.getQuestion());
        }
        if (wrapper.getAnswerOptions() != null) {
            jgen.writeArrayFieldStart("answerOptions");
            for (AnswerOption option : wrapper.getAnswerOptions()) {
                jgen.writeObject(option);
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }
    /*
    private QuestionSeries questionSeries;
    private String studentNumber;
    private Question question;
    private List<AnswerOption> answerOptions;
     */

}
