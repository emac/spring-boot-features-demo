package cn.emac.demo.petstore.common.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

/**
 * @author Emac
 * @since 2016-10-01
 */
public class Java8Mapper extends ObjectMapper {

    public Java8Mapper() {
        this(null, null, null);
    }

    public Java8Mapper(JsonFactory jf) {
        this(jf, null, null);
    }

    public Java8Mapper(ObjectMapper src) {
        super(src);

        init();
    }

    public Java8Mapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        super(jf, sp, dc);

        init();
    }

    private void init() {
        // serialization
        setSerializationInclusion(NON_ABSENT)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                // deserialization
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                // modules
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }
}
