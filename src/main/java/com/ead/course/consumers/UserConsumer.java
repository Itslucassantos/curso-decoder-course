package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDto;
import com.ead.course.enums.ActionType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    private final UserService userService;

    @Autowired
    public UserConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(bindings = @QueueBinding(
            // para criar a fila e acessar ela com os nomes definidos no yml. E se o exchange não tiver sido criado ele cria agr.
            value = @Queue(value = "${ead.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT,
                    ignoreDeclarationExceptions = "true")
    ))
    // recebe um @Payload UserEventDto, como corpo da mensagem enviada.
    public void listenUserEvent(@Payload UserEventDto userEventDto) {
        UserModel userModel = userEventDto.convertToUserModel();
        // valueOf() é para converter a string.
        switch (ActionType.valueOf(userEventDto.getActionType())) {
            // Se create ou update ele salva, como é a msm ação essa estrutura junta é melhor.
            case CREATE:
            case UPDATE:
                this.userService.save(userModel);
                break;
            case DELETE:
                this.userService.delete(userEventDto.getUserId());
                break;
        }
    }

}
