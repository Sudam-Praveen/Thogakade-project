package dto;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class OrderDetailsDto {
    private String orderId;
    private String itemCode;
    private int qty;
    private double unitPrice;


}