package controller;

import DAO.util.BoType;
import DB.DBConnection;
import bo.BoFactory;
import bo.Custom.CustomerBo;
import bo.Custom.Impl.CustomerBoImpl;
import dto.CustomerDto;
import dto.tm.CustomerTm;
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
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerFormController {

    public TextField txtSearch;
    public Label lblTime;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSalary;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colOption;
    private CustomerBo customerBo= BoFactory.getInstance().getBoFactory(BoType.CUSTOMER);
    private ObservableList<CustomerTm> tmList;




    public void reloadButtonOnAction(javafx.event.ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        loadCustomerTable();
        tblCustomer.refresh();
        clearFields();
    }

    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean isSaved = customerBo.updateCustomer(new CustomerDto(txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        ));
        if (isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Customer Updated!").show();
            loadCustomerTable();
            clearFields();
        }

    }
    public void initialize() throws SQLException, ClassNotFoundException {
        calculateTime();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadCustomerTable();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData(newValue);
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCustomers(newValue);
        });
    }
    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private  void filterCustomers(String filter) {
        FilteredList<CustomerTm> filteredList = new FilteredList<>(tmList, customer ->
                customer.getId().toLowerCase().contains(filter.toLowerCase()) ||
                        customer.getName().toLowerCase().contains(filter.toLowerCase()) ||
                customer.getAddress().toLowerCase().contains(filter.toLowerCase()));

        tblCustomer.setItems(filteredList);
    }


    private void setData(CustomerTm newValue) {
        if (newValue != null) {
            txtId.setEditable(false);
            txtId.setText(newValue.getId());
            txtName.setText(newValue.getName());
            txtAddress.setText(newValue.getAddress());
            txtSalary.setText(String.valueOf(newValue.getSalary()));
        }
    }
    private void loadCustomerTable() throws SQLException, ClassNotFoundException {
         tmList = FXCollections.observableArrayList();

        List<CustomerDto> dtoList = customerBo.getAllCustomers();

        for (CustomerDto dto:dtoList) {
            Button btn = new Button("Delete");
            btn.setStyle("-fx-background-color: #F94C10; -fx-text-fill: #FFF6F6;");
            CustomerTm c = new CustomerTm(
                    dto.getId(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getSalary(),
                    btn
            );

            btn.setOnAction(actionEvent -> {
                deleteCustomer(c.getId());
            });

            tmList.add(c);
        }

        tblCustomer.setItems(tmList);
    }
    private void deleteCustomer(String id) {
        try {
            boolean isDeleted = customerBo.deleteCustomer(id);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted!").show();
                loadCustomerTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void backButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) tblCustomer.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {
        boolean isSaved = false;
        try {
            isSaved = customerBo.saveCustomer(new CustomerDto(txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    Double.parseDouble(txtSalary.getText())
            ));
        }catch(SQLIntegrityConstraintViolationException e){
            new Alert(Alert.AlertType.ERROR,"Duplicate ID").show();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved!").show();
            try {
                loadCustomerTable();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            clearFields();
        }

    }
    private void clearFields() {
        tblCustomer.refresh();
        txtSalary.clear();
        txtAddress.clear();
        txtName.clear();
        txtId.clear();
        txtId.setEditable(true);
    }

    public void CustomerReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/Customer_Report.jrxml");
            //
            //
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



