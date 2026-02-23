package com.order.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {
	
//	private static final String TOPIC = null;
	private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;
	
	public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
		log.info("Sending order confirmation");
//		Message<OrderConfirmation> message = MessageBuilder.withPayload(orderConfirmation).setHeader(KafkaHeaders.TOPIC, "order-topic").build();
		Message<OrderConfirmation> message = MessageBuilder.withPayload(orderConfirmation).setHeader(TOPIC, "order-topic").build();
		log.info("Order send successfully");
		var future = kafkaTemplate.send(message);

        // 2. Add a callback to handle Success or Failure
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                // SUCCESS
                log.info("✅ SUCCESS! Sent message=[" + orderConfirmation.orderReference() + 
                         "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                // FAILURE
                log.error("❌ FAILURE! Unable to send message due to : " + ex.getMessage());
            }
        });
	}
}
