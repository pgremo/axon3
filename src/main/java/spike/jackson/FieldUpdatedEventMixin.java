package spike.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldUpdatedEventMixin {
    public FieldUpdatedEventMixin(@JsonProperty("id") String id) {
    }
}
