package keysson.apis.msgsend.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public class NullSafeMessageConverter extends SimpleMessageConverter {

    @Override
    public Object fromMessage(Message message) throws org.springframework.amqp.support.converter.MessageConversionException {
        if (message.getMessageProperties().getContentType() == null) {
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return super.fromMessage(message);
    }
}
