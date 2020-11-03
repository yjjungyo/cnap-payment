package bookmarket;

import bookmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @Autowired
    PaymentRepository paymentRepository;
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_PayCancel(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener PayCancel : " + orderCanceled.toJson());

            Optional<Payment> paymentOptional = paymentRepository.findById(orderCanceled.getPaymentId());
            Payment payment = paymentOptional.get();
            payment.setStatus(orderCanceled.getStatus());

            paymentRepository.save(payment); //repository에서 find 하고 save 하면 Update 이벤트가 발생된다.



            //배송 Aggregate Record 등록
            PayCanceled payCanceled = new PayCanceled();
            payCanceled.setOrderId(orderCanceled.getId());
            payCanceled.setDeliveryId(orderCanceled.getDeliveryId());
            payCanceled.setStatus("PayCanceled");

            payCanceled.publish();




        }
    }

}
