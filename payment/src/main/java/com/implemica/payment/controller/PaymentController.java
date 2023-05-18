package com.implemica.payment.controller;

import com.google.gson.Gson;
import com.implemica.payment.model.CustomPayment;
import com.implemica.payment.model.PaymentDTO;
import com.implemica.payment.model.PaymentStatus;
import com.implemica.payment.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final String BASE_URL = "http://localhost:9000/";
    private final PaymentService paymentService;
    public static final String SUCCESS_URL = "http://localhost:8080/payment/success";
    public static final String CANCEL_URL = "http://localhost:8080/payment/cancel";

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    public String payment(@RequestBody PaymentDTO paymentDTO) {
        try {
            Payment payment = paymentService.createPayment(paymentDTO.getPaymentTotalSum(), "USD", "paypal",
                "sale", "Payment for cars in the car shop CarsCatalog",  CANCEL_URL,
                 SUCCESS_URL+"/"+paymentDTO.getOrderId()+"/");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return new Gson().toJson("redirect:"+link.getHref());
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new Gson().toJson("redirect:/");
    }

    @GetMapping(value = "/cancel")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = "/success/{orderId}/")
    public ModelAndView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @PathVariable("orderId") Long orderId) {
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);

            PaymentDTO customPayment = new PaymentDTO(null, orderId, new Date(),
                PaymentStatus.valueOf(payment.getState().toUpperCase()),
                Double.valueOf(payment.getTransactions()
                .get(0).getRelatedResources().get(0).getSale().getAmount().getTotal()));

            paymentService.save(customPayment);
            if (payment.getState().equals("approved")) {
                paymentService.makeOrderDone(orderId);
                return new ModelAndView("redirect:" + BASE_URL+"order/"+ orderId);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return new ModelAndView("redirect:" + BASE_URL+"error/payment");
    }




































    @PostMapping(value = "/get/{id}")
    public ResponseEntity<CustomPayment> getCars(@PathVariable("id") Long id){
        CustomPayment cartCars = paymentService.getPayment(id);

        return new ResponseEntity<>(cartCars, HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addPayment(@RequestBody PaymentDTO payment){
        //TODO implement payment thought paypal and save with different statuses depend on result
        paymentService.save(payment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteCarFromCart(@PathVariable("id") Long id){
        paymentService.delete(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
