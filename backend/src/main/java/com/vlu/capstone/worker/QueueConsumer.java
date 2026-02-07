package com.vlu.capstone.worker;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@Profile("!test")
public class QueueConsumer {

    private final Map<String, JobProcessor> processors;

    public QueueConsumer(java.util.List<JobProcessor> processorList) {
        this.processors = processorList.stream()
                .collect(java.util.stream.Collectors.toMap(p -> p.getType().name(), p -> p));
    }

    @RabbitListener(queues = "${rabbitmq.queue.job}")
    public void consume(Job job, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("Processing job: type={}, id={}", job.getType(), job.getId());
            JobProcessor p = processors.get(job.getType().name());
            if (p == null) {
                log.error("No processor for type: {}", job.getType());
                channel.basicNack(tag, false, false);
                return;
            }
            p.process(job);
            channel.basicAck(tag, false);
            log.info("Job done: {}", job.getId());
        } catch (Exception e) {
            log.error("Job failed: {}", job.getId(), e);
            if (job.getRetryCount() < 3) {
                job.incrementRetry();
                channel.basicNack(tag, false, true);
            } else {
                channel.basicNack(tag, false, false);
            }
        }
    }
}
