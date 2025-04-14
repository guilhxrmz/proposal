package br.com.palo.ti.proposal.producers;

import br.com.palo.ti.proposal.dtos.statusDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class candidateProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.proposal.status}")
    private String routingKey;

    public candidateProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishStatus(statusDto status) {
        rabbitTemplate.convertAndSend("", routingKey, status);
    }
}