package DAO;

import DAO.Custom.Impl.CustomerDAOImpl;
import DAO.Custom.Impl.ItemDAOImpl;
import DAO.Custom.Impl.OrderDAOImpl;
import DAO.util.DaoType;

public class DaoFactory {
    public static DaoFactory daoFactory;
    private DaoFactory(){

    }
    public static DaoFactory getInstance(){
        return daoFactory != null ? daoFactory : (daoFactory=new DaoFactory());
    }
    public <T extends SuperDAO>T getDaoFactory(DaoType type){
        switch (type){
            case CUSTOMER:return (T) new CustomerDAOImpl();
            case ITEM : return (T)new ItemDAOImpl();
            case ORDER:return (T)new OrderDAOImpl();

        }
        return null;
    }
}
