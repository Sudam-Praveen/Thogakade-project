package bo.Custom;

import bo.SuperBo;
import dto.OrderDto;

import java.sql.SQLException;

public interface OrderBo extends SuperBo {
    public boolean saveOrder(OrderDto dto) throws SQLException, ClassNotFoundException;
    public String generateID();

}
