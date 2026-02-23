package com.Notification_Service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.Notification_Service.order.OrderConfirmation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;
	
	@Async
	public void sendCustomerConfirmationMail(OrderConfirmation confirmation) throws MessagingException {
		log.info("Sending email to "+confirmation.customer().fullName());
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
		helper.setFrom(sender);
		helper.setTo(confirmation.customer().username());
		helper.setSubject("Order Confirmation "+confirmation.customer().username());
		
		String htmlBody = String.format("""
                <div style="font-family: Arial, sans-serif; color: #333;">
                <h1>Order Successfully Placed!</h1>
                <p>Hi %s,</p>
                <p>Thank you for your order. Here are the details:</p>
                <div style="border: 1px solid #ddd; padding: 10px; border-radius: 5px;">
                    <p><strong>Order Ref:</strong> %s</p>
                    <p><strong>Total Amount:</strong> $%.2f</p>
                </div>
                <br/>
                <p>We are processing it now!</p>
            </div>
            """, confirmation.customer().fullName(),confirmation.orderReference(),confirmation.totalAmount());
		helper.setText(htmlBody, true);
		
		javaMailSender.send(mimeMessage);
		log.info("Email send successfully to "+confirmation.customer().username());
		
		
	}
	
}
