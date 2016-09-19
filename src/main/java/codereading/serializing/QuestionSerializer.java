package codereading.serializing;

import codereading.domain.Question;
import codereading.domain.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class QuestionSerializer extends StdSerializer<Question> {

    public QuestionSerializer() {
        this(null);
    }

    public QuestionSerializer(Class<Question> q) {
        super(q);
    }

    @Override
    public void serialize(Question question, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", question.getId());
        jgen.writeEndObject();
    }
}