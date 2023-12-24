package bo.Custom.Impl;

import DAO.Custom.ItemDAO;
import DAO.DaoFactory;
import DAO.util.DaoType;
import bo.Custom.ItemBo;
import dto.ItemDto;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {
    private final ItemDAO itemDao = DaoFactory.getInstance().getDaoFactory(DaoType.ITEM);

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new Item(
                dto.getCode(),
                dto.getDesc(),
                dto.getUnitPrice(),
                dto.getQtyInHand()
        ));

    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                dto.getCode(),
                dto.getDesc(),
                dto.getUnitPrice(),
                dto.getQtyInHand()
        ));
    }

    @Override
    public boolean deleteItem(String value) throws SQLException, ClassNotFoundException {
        return itemDao.delete(value);

    }

    @Override
    public List<ItemDto> getAllItem() throws SQLException, ClassNotFoundException {
        List<ItemDto> list =new ArrayList<>();
        List<Item> all = itemDao.getAll();
        for (Item item:all) {
           list.add(new ItemDto(
                   item.getCode(),
                   item.getDescription(),
                   item.getUnitPrice(),
                   item.getQtyOnHand()
           ));
        }
        return list;
    }
}
