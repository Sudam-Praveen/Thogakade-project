package dto;

import lombok.*;

@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private String code;
    private String desc;
    private double unitPrice;
    private int qtyInHand;
}
