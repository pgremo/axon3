package spike;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateAggregateCommand {
    @TargetAggregateIdentifier
    private String id;

    public CreateAggregateCommand(String id) {
        System.out.println("Command ctor is = " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
