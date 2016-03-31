package cn.emac.demo.petstore.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Optional;

public class OptionalSerializer<T> extends JsonSerializer<Optional<T>> {

    @Override
    public void serialize(Optional<T> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null && value.isPresent()) {
            gen.writeObject(value.get());
        }
    }

}
