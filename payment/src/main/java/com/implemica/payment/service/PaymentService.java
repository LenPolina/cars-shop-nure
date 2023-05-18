package com.implemica.payment.service;

import com.implemica.payment.model.CustomPayment;
import com.implemica.payment.model.PaymentDTO;
import com.implemica.payment.repository.PaymentRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final String BASE_URL = "http://localhost:8080/";
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final APIContext apiContext;
    private final RestTemplate restTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentService(ModelMapper modelMapper, PaymentRepository paymentRepository, APIContext apiContext, RestTemplate restTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
        this.apiContext = apiContext;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Payment createPayment(
        Double total,
        String currency,
        String method,
        String intent,
        String description,
        String cancelUrl,
        String successUrl) throws PayPalRESTException{

        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        apiContext.setMaskRequestId(true);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public CustomPayment getPayment(Long id){
        log.info("Getting the payment from db = {}", id);
        return paymentRepository.findById(id).get();
    }

    public void save(PaymentDTO payment) {
        CustomPayment newCart = paymentRepository.save(convertTo(payment));
        log.info("Add payment to db = {}", newCart.getId());
    }
    public void delete(Long Id) {
        paymentRepository.deleteById(Id);
    }
    public CustomPayment convertTo(PaymentDTO payment) {
       return modelMapper.map(payment, CustomPayment.class);
    }

    public PaymentDTO convertTo(CustomPayment customPayment) {
        return modelMapper.map(customPayment, PaymentDTO.class);
    }

    public void makeOrderDone(Long orderId) {
        kafkaTemplate.send("HandleOrder",orderId.toString());
       //restTemplate.getForEntity(BASE_URL+"order/execute/order/"+orderId,Void.class);
    }
}
