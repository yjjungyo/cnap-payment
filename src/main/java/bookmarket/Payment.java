package bookmarket;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String status;
    private Long customerId;

    @PostPersist
    public void onPostPersist(){

        try{
            System.out.println("................ Order . PrePersist ...... sleep .....");
            Thread.currentThread().sleep((long)(800+Math.random()*220));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }


        Paid paid = new Paid();
        System.out.println("Payment: paid.getOrderId()="+paid.getOrderId());
        paid.setStatus("Paied");
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();


    }

    @PreUpdate
    public void onPreUpdate(){
        PayCanceled payCanceled = new PayCanceled();
        BeanUtils.copyProperties(this, payCanceled);
        payCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }




}
