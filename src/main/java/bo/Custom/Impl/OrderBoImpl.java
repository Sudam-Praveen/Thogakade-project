package bo.Custom.Impl;

import DAO.Custom.Impl.OrderDAOImpl;
import DAO.Custom.OrderDAO;
import DAO.DaoFactory;
import DAO.util.DaoType;
import bo.Custom.OrderBo;
import dto.OrderDto;

import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {
    private final OrderDAO orderDAO = DaoFactory.getInstance().getDaoFactory(DaoType.ORDER);
    @Override
    public boolean saveOrder(OrderDto dto) throws SQLException, ClassNotFoundException {
        return orderDAO.save(dto);
    }
    public String generateID(){
        OrderDto dto = orderDAO.getLastOrder();
        if (dto != null) {
            String id = dto.getOrderId();
            try {
                int num = Integer.parseInt(id.split("D")[1]);
                num++;
                return (String.format("D%03d", num));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

                return ("D001");
            }
        } else {
            return ("D001");
        }
    }
}
