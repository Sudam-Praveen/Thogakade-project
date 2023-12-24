package entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
@Entity
public class OrderDetail {


    @EmbeddedId
    private OrderDetailsKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Orders orders;

    @ManyToOne
    @MapsId("itemCode")
    @JoinColumn(name = "item_code")
    Item item;

    private int qty;
    private double unitPrice;

}
