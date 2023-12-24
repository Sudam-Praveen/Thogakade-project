package DAO.Custom;

import DAO.CrudDAO;
import dto.CustomerDto;
import entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO <Customer>{

    boolean save(Customer dto) throws SQLException, ClassNotFoundException;

    boolean update(Customer dto) throws SQLException, ClassNotFoundException;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    List<Customer> getAll() throws SQLException, ClassNotFoundException;

    CustomerDto searchCustomer(String id);
}
