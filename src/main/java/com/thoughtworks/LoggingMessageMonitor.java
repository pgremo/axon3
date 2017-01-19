package com.thoughtworks;

import org.axonframework.messaging.Message;
import org.axonframework.monitoring.MessageMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMessageMonitor implements MessageMonitor<Message<?>> {
  private Logger logger = LoggerFactory.getLogger(MessageMonitor.class);

  @Override
  public MonitorCallback onMessageIngested(Message message) {
    return new MonitorCallback() {
      @Override
      public void reportSuccess() {
        logger.info("succeeded processing command");
      }

      @Override
      public void reportFailure(Throwable cause) {
        logger.error("failed processing command", cause);
      }

      @Override
      public void reportIgnored() {
        logger.info("ignored?");
      }
    };
  }
}
