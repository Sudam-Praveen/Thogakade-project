package controller;

import DAO.util.BoType;
import DB.DBConnection;
import bo.BoFactory;
import bo.Custom.ItemBo;
import dto.ItemDto;
import dto.tm.ItemTm;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static net.sf.jasperreports.engine.JasperCompileManager.compileReport;

public class ItemFormController {

    public TextField txtSearch;
    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TableView<ItemTm> tblItem;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colOption;

    @FXML
    private Label tblTime;
    private final ItemBo itemBo = BoFactory.getInstance().getBoFactory(BoType.ITEM);
    private ObservableList<ItemTm> tmList;

    public void initialize() throws SQLException, ClassNotFoundException {
        calculateTime();
        loadItemTable();
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyInHand"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, itemTm, newValue) -> {
            setData(newValue);
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
           filterItems(newValue);
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
    private void filterItems(String filter) {
        FilteredList<ItemTm> filteredList = new FilteredList<>(tmList, item ->
                item.getCode().toLowerCase().contains(filter.toLowerCase()) ||
                        item.getDesc().toLowerCase().contains(filter.toLowerCase()));

        tblItem.setItems(filteredList);
    }

    private void setData(ItemTm newValue) {
        if (newValue != null) {
            txtCode.setEditable(false);
            txtCode.setText(newValue.getCode());
            txtDesc.setText(newValue.getDesc());
            txtPrice.setText(String.valueOf(newValue.getUnitPrice()));
            txtQty.setText(String.valueOf(newValue.getQtyInHand()));
        }
    }

    private void loadItemTable() throws SQLException, ClassNotFoundException {
         tmList = FXCollections.observableArrayList();
        List<ItemDto> allItem = itemBo.getAllItem();

        for (ItemDto dto : allItem) {
            Button btn = new Button("Delete");
            btn.setStyle("-fx-background-color: #F94C10; -fx-text-fill: #FFF6F6;");
            ItemTm tm = new ItemTm(
                    dto.getCode(),
                    dto.getDesc(),
                    dto.getUnitPrice(),
                    dto.getQtyInHand(),
                    btn
            );
            btn.setOnAction(actionEvent -> {
               deleteItemFromTable(dto.getCode());
            });
            tmList.add(tm);
        }
        tblItem.setItems(tmList);

    }

    private void deleteItemFromTable(String code) {
        try {
            boolean isDeleted = itemBo.deleteItem(code);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted!").show();
                loadItemTable();
                clearField();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean saved = false;
        try {
            saved = itemBo.saveItem(new ItemDto(
                    txtCode.getText(),
                    txtDesc.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText())
            ));
        } catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot save ID Duplicate").show();
        }
        if (saved) {
            new Alert(Alert.AlertType.CONFIRMATION, "Item Saved").show();
            loadItemTable();
            clearField();
        }
    }

        public void updateOnAction (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

            boolean updated = itemBo.updateItem(new ItemDto(
                    txtCode.getText(),
                    txtDesc.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText())
            ));

            if (updated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item Updated").show();
                loadItemTable();
                clearField();
            }
        }

    private void clearField() {
        tblItem.refresh();
        txtCode.clear();
        txtDesc.clear();
        txtPrice.clear();
        txtQty.clear();
        txtCode.setEditable(true);
    }

    public void backButtonOnAction (ActionEvent actionEvent){
            Stage stage = (Stage) tblItem.getScene().getWindow();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void itemReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/Item_Report.jrxml");
            //
            //
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

}
