package bo;

import DAO.util.BoType;
import bo.Custom.Impl.CustomerBoImpl;
import bo.Custom.Impl.ItemBoImpl;
import bo.Custom.Impl.OrderBoImpl;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory(){

    }
    public static BoFactory getInstance(){
        return boFactory!=null ? boFactory : (boFactory=new BoFactory());
    }

    public <T extends SuperBo>T getBoFactory(BoType type) {
        switch(type){
            case CUSTOMER:return (T) new CustomerBoImpl();
            case ITEM : return (T) new ItemBoImpl();
            case ORDER:return (T)new OrderBoImpl();
        }
        return null;
    }
}
