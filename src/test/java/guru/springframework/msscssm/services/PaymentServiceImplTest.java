package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/*
 * Created by arunabhamidipati on 27/12/2019
 */

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository repository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().id(1234567L).amount(new BigDecimal("12.29")).build();
    }

    @Transactional
    @Test
    void preAuth() {

        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("Should be new");
        System.out.println(savedPayment.getState());

        paymentService.preAuth(savedPayment.getId());

        Payment preAuthPayment = repository.getOne(savedPayment.getId());

        System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(preAuthPayment.getState());

    }

    @Transactional
    @Test
    void authorize(){
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("SHOULD BE NEW");
        System.out.println(savedPayment.getState());

        savedPayment.setState(PaymentState.PRE_AUTH);
        Payment preAuthPayment = repository.getOne(savedPayment.getId());


        System.out.println("SHOULD BE PRE_AUTH");
        System.out.println(preAuthPayment.getState());

        paymentService.authorize(preAuthPayment.getId());

        Payment authPayment = repository.getOne(preAuthPayment.getId());
        System.out.println("SHOULD BE AUTHORIZE OR AUTHORIZE_ERROR");
        System.out.println(authPayment.getState());;
    }
}