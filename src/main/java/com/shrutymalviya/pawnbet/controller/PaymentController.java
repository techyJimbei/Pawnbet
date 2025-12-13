//package com.shrutymalviya.pawnbet.controller;
//
//import com.shrutymalviya.pawnbet.pojos.PaymentRequestDTO;
//import com.stripe.Stripe;
//import com.stripe.model.PaymentIntent;
//import com.stripe.param.PaymentIntentCreateParams;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentController {
//
//    @Value("${stripe.secret.key}")
//    private String stripeSecretKey;
//
//    @PostMapping("/create-payment-intent")
//    public Map<String, String> createPaymentIntent(@RequestBody PaymentRequestDTO request) throws Exception {
//        Stripe.apiKey = stripeSecretKey;
//
//        long amount = request.getAmount() * 100;
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount)
//                .setCurrency("inr")
//                .build();
//
//        try {
//            PaymentIntent intent = PaymentIntent.create(params);
//            Map<String, String> response = new HashMap<>();
//            response.put("clientSecret", intent.getClientSecret());
//            return response;
//        } catch (Exception e) {
//            throw new RuntimeException("PaymentIntent creation failed: " + e.getMessage());
//        }
//
//    }
//
//}
