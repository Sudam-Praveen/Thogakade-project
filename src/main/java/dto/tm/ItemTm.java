package dto.tm;

import dto.ItemDto;
import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ItemTm {
    private String code;
    private String desc;
    private double unitPrice;
    private int qtyInHand;
    private Button btn;
}
