package bo.Custom.Impl;

import DAO.Custom.CustomerDAO;
import DAO.Custom.Impl.CustomerDAOImpl;
import DAO.DaoFactory;
import DAO.util.DaoType;
import bo.Custom.CustomerBo;
import dto.CustomerDto;
import entity.Customer;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {
    private final CustomerDAO customerDAO = DaoFactory.getInstance().getDaoFactory(DaoType.CUSTOMER);
    @Override
    public boolean saveCustomer(CustomerDto dto)  {

        try {
            return customerDAO.save(new Customer(
                    dto.getId(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getSalary()
            ));
        }catch(SQLIntegrityConstraintViolationException e){
            new Alert(Alert.AlertType.ERROR,"Duplicate ID").show();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {

        List<Customer> all = customerDAO.getAll();
        List <CustomerDto> dtoList=new ArrayList<>();
        for (Customer dto:all) {
            dtoList.add(new CustomerDto(
                    dto.getId(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getSalary()
            ));

        }
        return dtoList;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {

        return customerDAO.delete(id);
    }

    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary()
        ));
    }
}
