package entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
@Embeddable
public class OrderDetailsKey implements Serializable {

    @Column(name="order_id")
    private String orderId;
    @Column(name="item_code")
    private String itemCode;




}
