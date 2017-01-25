package spike.spring;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

class TransactionChannelInterceptor extends ChannelInterceptorAdapter {
  private static final DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
  private TransactionStatus status;
  private PlatformTransactionManager manager;

  TransactionChannelInterceptor(PlatformTransactionManager manager) {
    this.manager = manager;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    status = manager.getTransaction(definition);
    return message;
  }

  @Override
  public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
    if (ex == null) manager.commit(status);
    else manager.rollback(status);
  }
}
