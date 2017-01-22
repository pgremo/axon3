package spike;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/foo")
public class SpikeController {
  private final CommandGateway commandGateway;

  @Autowired
  public SpikeController(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @RequestMapping(value = "/create/{id}", method = GET)
  @ResponseStatus(OK)
  @Transactional
  public void fooBar(@PathVariable String id) {
    commandGateway.sendAndWait(new CreateAggregateCommand(id));
  }

  @RequestMapping(value = "/update/{id}", method = GET)
  @ResponseStatus(OK)
  public void fooBarUpdated(@PathVariable String id) {
    commandGateway.sendAndWait(new UpdateAggregateCommand(id));
  }
}
