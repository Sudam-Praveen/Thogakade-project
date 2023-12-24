package bo.Custom;

import bo.SuperBo;
import dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemBo extends SuperBo {
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String value) throws SQLException, ClassNotFoundException;
    List<ItemDto> getAllItem() throws SQLException, ClassNotFoundException;
}
