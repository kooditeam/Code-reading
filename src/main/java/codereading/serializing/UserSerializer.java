package codereading.serializing;

import codereading.domain.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> u) {
        super(u);
    }

    @Override
    public void serialize(User user, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        if (user.getId() != null) {
            jgen.writeNumberField("id", user.getId());
        } else {
            jgen.writeNullField("id");
        }
        jgen.writeEndObject();
    }
}