package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.Button;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderTm extends RecursiveTreeObject<OrderTm> {
    private String code;
    private String desc;
    private int qty;
    private double amount;
    private Button btn;
}