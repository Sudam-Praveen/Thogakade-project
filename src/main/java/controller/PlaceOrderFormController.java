package controller;

import DAO.DaoFactory;
import DAO.util.BoType;
import DAO.util.DaoType;
import DB.DBConnection;
import bo.BoFactory;
import bo.Custom.CustomerBo;
import bo.Custom.ItemBo;
import bo.Custom.OrderBo;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrderDto;
import dto.tm.OrderTm;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private ComboBox<String> cmbCusId;

    @FXML
    private ComboBox<String> cmbitemCode;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtQty;

    @FXML
    private TableView<OrderTm> tblCart;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colOption;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblTotal;

    @FXML
    private Label tblTime;
    private final ObservableList<OrderTm> tmlist = FXCollections.observableArrayList();
    private  CustomerBo customerBo = BoFactory.getInstance().getBoFactory(BoType.CUSTOMER);
    private  ItemBo itemBo = BoFactory.getInstance().getBoFactory(BoType.ITEM);
    private List<ItemDto> items;
    private List<CustomerDto> customers;
    private double tot = 0;
    private final OrderBo orderBo = BoFactory.getInstance().getBoFactory(BoType.ORDER);

    public void initialize(){
        calculateTime();
        loadItemCode();
        loadCustomerID();
        idGenerate();
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));


        cmbCusId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, id) -> {
            for (CustomerDto dto:customers) {
                if(dto.getId().equals(id)){
                    txtName.setText(dto.getName());
                }

            }
        });
        cmbitemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, code) -> {
            for (ItemDto dto:items) {
                if(dto.getCode().equals(code)){
                    txtDesc.setText(dto.getDesc());
                    txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
                }
            }
        });
    }
    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> tblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadItemCode() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            items = itemBo.getAllItem();
            for (ItemDto dto:items) {
                list.add(dto.getCode());
            }
            cmbitemCode.setItems(list);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerID() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {

            customers = customerBo.getAllCustomers();
            for (CustomerDto dto:customers) {
                list.add(dto.getId());

            }
            cmbCusId.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void addToCartButtonOnAction(ActionEvent actionEvent) {
        ItemDto item = null;
        for (ItemDto itemDto:items) {
            if(cmbitemCode.getValue().toString().equals(itemDto.getCode())){
               item = itemDto;
            }
        }
        double amount = item.getUnitPrice() * Integer.parseInt(txtQty.getText());
        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color: #F94C10; -fx-text-fill: #FFF6F6;");


        OrderTm orderTm = new OrderTm(
                cmbitemCode.getValue().toString(),
                txtDesc.getText(),
                Integer.parseInt(txtQty.getText()),
                amount,
                btn

        );
        btn.setOnAction(actionEvent1 -> {
            tmlist.remove(orderTm);
            tot -= orderTm.getAmount();
            tblCart.refresh();
            lblTotal.setText(String.format("%.2f", tot));
        });

        boolean isExist = false;
        for (OrderTm order : tmlist) {
            if (order.getCode().equals(orderTm.getCode())) {
                if ((orderTm.getQty() + order.getQty()) <= item.getQtyInHand()) {

                    order.setQty(order.getQty() + orderTm.getQty());
                    order.setAmount(order.getAmount() + orderTm.getAmount());
                    tblCart.refresh();
                    tot += orderTm.getAmount();

                } else {
                    new Alert(Alert.AlertType.ERROR, "QTY On Hand is : " + (item.getQtyInHand() - order.getQty())).show();
                }
                isExist = true;
            }

        }

        if (!isExist) {
            if (orderTm.getQty() <= item.getQtyInHand()) {
                tmlist.add(orderTm);
                tot += orderTm.getAmount();
            } else {
                new Alert(Alert.AlertType.ERROR, "QTY On Hand is : " + (item.getQtyInHand() - orderTm.getQty())).show();
            }
        }

        lblTotal.setText(String.format("%.2f", tot));
        tblCart.setItems(tmlist);

    }

    private void idGenerate() {
       lblOrderId.setText(orderBo.generateID());
    }


    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        List<OrderDetailsDto> list = new ArrayList<>();
        if (!tmlist.isEmpty()) {
            for (OrderTm tm : tmlist) {
                list.add(new OrderDetailsDto(
                        lblOrderId.getText(),
                        tm.getCode(),
                        tm.getQty(),
                        tm.getAmount() / tm.getQty()
                ));
            }
            boolean saved = false;
            try {
                saved = orderBo.saveOrder(new OrderDto(
                        lblOrderId.getText(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                        cmbCusId.getValue().toString(),
                        list
                ));
                if (saved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Saved").show();
                    clearFields();
                    idGenerate();
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Cart is empty").show();
        }


    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) tblCart.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
        stage.show();
    }
    private void clearFields() {
        cmbitemCode.setValue("");
        cmbCusId.setValue("");
        txtName.clear();
        txtDesc.clear();
        txtQty.clear();
        txtUnitPrice.clear();
        tmlist.clear();
    }

    public void reportOnAction(ActionEvent actionEvent) {

    }
}
