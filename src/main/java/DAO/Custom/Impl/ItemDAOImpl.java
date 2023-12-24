package DAO.Custom.Impl;

import DAO.Custom.ItemDAO;
import DAO.util.CrudUtil;
import DAO.util.HibernateUtil;
import entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean save(Item dto) throws SQLException, ClassNotFoundException {
//        String sql = "INSERT INTO Item VALUES(?,?,?,?)";
//        return  CrudUtil.getCrudUtil().execute(sql,dto.getCode(),dto.getDescription(),dto.getUnitPrice(),dto.getQtyOnHand());
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(dto);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Item dto) throws SQLException, ClassNotFoundException {
//        String sql = "UPDATE  Item SET description=? , unitPrice=?, qtyOnHand=? WHERE code=?";
//        return CrudUtil.getCrudUtil().execute(sql,dto.getDescription(),dto.getUnitPrice(),dto.getQtyOnHand(),dto.getCode());
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Item item = session.find(Item.class, dto.getCode());
        item.setDescription(dto.getDescription());
        item.setUnitPrice(dto.getUnitPrice());
        item.setQtyOnHand(dto.getQtyOnHand());
        session.save(item);
        transaction.commit();
        session.close();
        return true;

    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Item item = session.find(Item.class, value);
        session.delete(item);
        transaction.commit();
        session.close();
        return true;

    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("FROM Item");
        List<Item> list = query.list();
//        String sql="SELECT * FROM Item";
//        ResultSet resultSet=CrudUtil.getCrudUtil().execute(sql);
//        List<Item> list =new ArrayList<>();
//        while (resultSet.next()){
//            list.add(new Item(
//                    resultSet.getString(1),
//                    resultSet.getString(2),
//                    resultSet.getDouble(3),
//                    resultSet.getInt(4)
//            ));
//        }
        return list;
    }
}
