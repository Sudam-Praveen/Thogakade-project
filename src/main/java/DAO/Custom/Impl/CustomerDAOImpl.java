package DAO.Custom.Impl;

import DAO.Custom.CustomerDAO;

import DAO.util.HibernateUtil;
import dto.CustomerDto;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean save(Customer dto) throws SQLIntegrityConstraintViolationException {
        Session session = HibernateUtil.getSession();

        Transaction transaction = session.beginTransaction();
        session.save(dto);
        transaction.commit();
        session.close();
        return true;

    }



    @Override
    public boolean update(Customer dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();

        Transaction transaction = session.beginTransaction();

        Customer customer = session.find(Customer.class, dto.getId());
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setSalary(dto.getSalary());

        session.save(customer);
        transaction.commit();
        session.close();
        return true;


    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(session.find(Customer.class,id));
        transaction.commit();
        session.close();
        return true;


    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {

        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Customer");
        List<Customer> list = query.list();

        session.close();
        return list;
    }

    @Override
    public CustomerDto searchCustomer(String id) {
        return null;
    }


}
