package com.vlu.capstone.worker;

import com.vlu.capstone.common.exception.BusinessException;
import com.vlu.capstone.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!test")
public class QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.job}")
    private String exchange;
    @Value("${rabbitmq.routing-key.job}")
    private String routingKey;

    public void publish(Job job) {
        if (job.getId() == null) {
            job.setId(UUID.randomUUID().toString());
        }
        try {
            log.info("Publishing job: type={}, id={}", job.getType(), job.getId());
            rabbitTemplate.convertAndSend(exchange, routingKey, job);
        } catch (Exception e) {
            log.error("Failed to publish job: {}", job.getId(), e);
            throw new BusinessException(ErrorCode.SYS_002);
        }
    }
}
