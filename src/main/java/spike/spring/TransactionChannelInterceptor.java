package spike.spring;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

class TransactionChannelInterceptor extends ChannelInterceptorAdapter {
  private TransactionStatus transaction;
  private PlatformTransactionManager transactionManager;

  TransactionChannelInterceptor(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
    return super.preSend(message, channel);
  }

  @Override
  public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
    if (ex == null) {
      transactionManager.commit(transaction);
    } else {
      transactionManager.rollback(transaction);
    }
    super.afterSendCompletion(message, channel, sent, ex);
  }
}
