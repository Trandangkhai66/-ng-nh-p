package com.vlu.capstone.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitConfig {

    @Value("${rabbitmq.queue.job}")
    private String jobQueue;
    @Value("${rabbitmq.queue.dlq}")
    private String dlq;
    @Value("${rabbitmq.exchange.job}")
    private String jobExchange;
    @Value("${rabbitmq.exchange.dlx}")
    private String dlx;
    @Value("${rabbitmq.routing-key.job}")
    private String jobRoutingKey;
    @Value("${rabbitmq.routing-key.dlq}")
    private String dlqRoutingKey;

    @Bean
    public Queue jobQueue() {
        return QueueBuilder.durable(jobQueue)
                .withArgument("x-dead-letter-exchange", dlx)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlq).build();
    }

    @Bean
    public DirectExchange jobExchange() {
        return new DirectExchange(jobExchange);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlx);
    }

    @Bean
    public Binding jobBinding() {
        return BindingBuilder.bind(jobQueue()).to(jobExchange()).with(jobRoutingKey);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(dlqRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
