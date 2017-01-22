package spike.jackson.axon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.SortedSet;

public class GapAwareTrackingTokenMixin {
    public GapAwareTrackingTokenMixin(@JsonProperty("index") long index, @JsonProperty("gaps")SortedSet<Long> gaps) {
    }
}
