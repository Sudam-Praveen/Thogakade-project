package DAO.Custom;

import DAO.CrudDAO;
import dto.ItemDto;
import entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item> {
    public boolean save(Item dto) throws SQLException, ClassNotFoundException;
    public boolean update(Item dto) throws SQLException, ClassNotFoundException;
    public boolean delete(String value) throws SQLException, ClassNotFoundException;
    List<Item> getAll() throws SQLException, ClassNotFoundException;
}
