package com.Notification_Service.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.Notification_Service.email.EmailService;
import com.Notification_Service.order.OrderConfirmation;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@RequiredArgsConstructor
public class NotificationConsumer {

    // NO fields here related to OrderConfirmation.
    // NO Repository (since you removed the database requirement).
	

	
	@Autowired
	EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) {
        
        // The object 'orderConfirmation' is passed IN by Kafka when a message arrives.
        // It is NOT injected by Spring at startup.
        
        log.info("###################################################");
        log.info("### 🟢 MESSAGE RECEIVED FROM KAFKA 🟢 ###");
        log.info("### Order Reference: {}", orderConfirmation.orderReference());
        log.info("### Amount: {}", orderConfirmation.totalAmount());
        
        if (orderConfirmation.customer() != null) {
             log.info("### Customer: {} {}", orderConfirmation.customer().fullName(), orderConfirmation.customer().username());
        } else {
             log.info("### Customer info is null");
        }
        
        log.info("#######################");
        
        log.info("📨 Email Service received order: {}", orderConfirmation.orderReference());

        try {
            emailService.sendCustomerConfirmationMail(orderConfirmation);
        } catch (MessagingException e) {
            log.error("❌ Failed to send email", e);
        }
        
        // Add email logic here later
    }
}
