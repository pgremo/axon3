package spike.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AggregateCreatedEventMixin {
    public AggregateCreatedEventMixin(@JsonProperty("id") String id, @JsonProperty("value") String value) {
    }
}
