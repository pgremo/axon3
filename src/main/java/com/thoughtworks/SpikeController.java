package com.thoughtworks;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/foo")
public class SpikeController {
    @Autowired
    CommandGateway commandGateway;

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
