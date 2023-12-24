package DAO.Custom.Impl;

import DAO.Custom.OrderDAO;
import DAO.DaoFactory;
import DAO.util.DaoType;
import DAO.util.HibernateUtil;
import DB.DBConnection;
import dto.OrderDetailsDto;
import dto.OrderDto;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {



    @Override
    public boolean save(OrderDto dto) throws SQLException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Orders orders = new Orders(
                dto.getOrderId(),
                dto.getDate()
        );
        orders.setCustomer(session.find(Customer.class,dto.getCustId()));
        session.save(orders);

        List<OrderDetailsDto> list = dto.getList();
        for (OrderDetailsDto detailsDto:list) {
            OrderDetail orderDetail = new OrderDetail(
                    new OrderDetailsKey(detailsDto.getOrderId(),detailsDto.getItemCode()),
                    orders,
                    session.find(Item.class,detailsDto.getItemCode()),
                    detailsDto.getQty(),
                    detailsDto.getUnitPrice()
            );
            session.save(orderDetail);

        }
        transaction.commit();
        return true;

//        Connection connection = null;
//        try {
//            connection = DBConnection.getInstance().getConnection();
//            connection.setAutoCommit(false);
//            String sql = "INSERT INTO Orders VALUES (?,?,?)";
//            PreparedStatement pstm = connection.prepareStatement(sql);
//            pstm.setString(1, dto.getOrderId());
//            pstm.setString(2, dto.getDate());
//            pstm.setString(3, dto.getCustId());
//            int result = pstm.executeUpdate();
//
//            if (result > 0) {
//                boolean saved = orderDetailDAO.saveOrderDetails(dto.getList());
//                if (saved) {
//                    connection.commit();
//                    return true;
//                }
//            }
//
//        } catch (SQLException | ClassNotFoundException e) {
//            connection.rollback();
//            e.printStackTrace();
//
//        } finally {
//            connection.setAutoCommit(true);
//        }
//
//        return false;
    }

    @Override
    public OrderDto getLastOrder() {
        String sql = "SELECT * FROM Orders ORDER BY id DESC LIMIT 1";
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = pstm.executeQuery();
            if (result.next()) {
                return new OrderDto(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        null
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public boolean update(OrderDto entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<OrderDto> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}

