package cn.emac.demo.petstore.domain.vacation;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * @author Emac
 * @since 2017-04-23
 */
@Data
public class VacationRequest {

    @NotNull
    private VacationType type;

    private String reason;

    @NotNull
    private OffsetDateTime start;

    @NotNull
    private OffsetDateTime end;

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();
        if (type != null) {
            map.put("type", type.getValue().toString());
        }

        if (StringUtils.isNotEmpty(reason)) {
            map.put("reason", reason);
        }

        if (start != null) {
            map.put("start", Long.toString(start.toInstant().toEpochMilli()));
        }

        if (end != null) {
            map.put("end", Long.toString(end.toInstant().toEpochMilli()));
        }

        return map;
    }
}
