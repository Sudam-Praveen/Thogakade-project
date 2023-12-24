package bo.Custom;

import bo.SuperBo;
import dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo  extends SuperBo {
    boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
    List<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
}
