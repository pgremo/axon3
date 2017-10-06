package spike;

public class AggregateCreatedEvent {
  private String id;
  private String value;

  public AggregateCreatedEvent(String id, String value) {


    this.id = id;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }
}
