//package com.Notification_Service.notifications;
//
//import java.time.LocalDateTime;
//
//import com.Notification_Service.notifications.payment.PaymentConfirmation;
//import com.Notification_Service.order.OrderConfirmation;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Builder
//public class Notification {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
//	private NotificationType notificationType;
//	private LocalDateTime notificationDate;
//	private OrderConfirmation orderConfirmation;
////	private PaymentConfirmation paymentConfirmation;
//}
