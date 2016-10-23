package codereading.serializing;

import codereading.domain.QuestionSeries;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class QuestionSeriesSerializer extends StdSerializer<QuestionSeries> {

    public QuestionSeriesSerializer() {
        this(null);
    }

    public QuestionSeriesSerializer(Class<QuestionSeries> q) {
        super(q);
    }

    @Override
    public void serialize(QuestionSeries series, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", series.getId());
        jgen.writeEndObject();
    }
}