package DAO.Custom;

import DAO.CrudDAO;
import DAO.SuperDAO;
import dto.OrderDto;

public interface OrderDAO extends CrudDAO<OrderDto> {

    OrderDto getLastOrder();
}
