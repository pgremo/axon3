package spike;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SpikeView {
  @Id
  private String id;
  private String value;

  public SpikeView() {
  }

  public SpikeView(String id, String value) {
    this.id = id;
    this.value = value;
  }
}
