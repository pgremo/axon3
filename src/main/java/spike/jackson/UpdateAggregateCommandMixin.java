package spike.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UpdateAggregateCommandMixin {
  public UpdateAggregateCommandMixin(@JsonProperty("id") String id) {
  }
}
