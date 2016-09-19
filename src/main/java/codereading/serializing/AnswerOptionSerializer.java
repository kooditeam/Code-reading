package codereading.serializing;

import codereading.domain.AnswerOption;
import codereading.domain.Question;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AnswerOptionSerializer extends StdSerializer<AnswerOption> {

    public AnswerOptionSerializer() {
        this(null);
    }

    public AnswerOptionSerializer(Class<AnswerOption> a) {
        super(a);
    }

    @Override
    public void serialize(AnswerOption option, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", option.getId());
        jgen.writeStartObject();
        jgen.writeNumberField("id", option.getQuestion().getId());
        jgen.writeEndObject();
        jgen.writeStringField("answerText", option.getAnswerText());
        jgen.writeEndObject();
    }
}