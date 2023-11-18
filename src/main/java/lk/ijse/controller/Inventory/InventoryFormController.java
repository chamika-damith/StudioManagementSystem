package lk.ijse.controller.Inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lk.ijse.dto.ItemDto;
import lk.ijse.dto.tm.CustomerTm;
import lk.ijse.dto.tm.ItemTm;
import lk.ijse.model.ItemModel;
import org.controlsfx.control.Notifications;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InventoryFormController {

    public JFXTextField txtid;
    public JFXTextField txtname;
    public JFXTextField txtqty;
    public JFXTextField txtprice;
    public JFXTextField txtdescription;
    public JFXComboBox txtcomboBox;

    public Image image;
    public FileInputStream fileInputStream;

    public ImageView img;
    public JFXButton imgSelectBtn;
    public Label txtAllInventory;
    public TableView tblItem;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colDescription;
    public TableColumn colCategory;
    public TableColumn colAction;

    public Label orderId;
    public JFXTextField txtSearchTable;
    public AnchorPane InventoryRoot;

    private ItemModel model=new ItemModel();

    private ObservableList<ItemTm> obList;


    public void initialize(){
        txtcomboBox.setItems(FXCollections.observableArrayList("CAMERA", "LENS", "DRONE", "LIGHTS", "ACCESORIES"));
        setValueLable();
        getAllItem();
        setCellValue();
        generateNextOrderId();
        searchTable();
    }

    private void generateNextOrderId() {
        try {
            int orderID = ItemModel.generateNextOrderId();
            orderId.setText(String.valueOf("00"+orderID));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public Button createButton(){
        Button btn=new Button("Delete");
        btn.getStyleClass().add("ActionBtn");
        btn.setCursor(Cursor.cursor("Hand"));
        setDeleteBtnAction(btn);
        return btn;
    }

    private void getAllItem() {
        var model=new ItemModel();

        obList=FXCollections.observableArrayList();

        try {
            List<ItemDto> allItems = model.getAllItems();

            for (ItemDto dto : allItems){
                Button button = createButton();
                obList.add(new ItemTm(
                        dto.getItemId(),
                        dto.getName(),
                        dto.getQty(),
                        dto.getPrice(),
                        dto.getDescription(),
                        dto.getCategory(),
                        button
                ));

            }
            tblItem.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValue() {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }


    public void setValueLable(){
        try {

            int count=0;
            List<ItemDto> allItems = model.getAllItems();

            for (ItemDto item : allItems){
                count+=1;
            }

            txtAllInventory.setText(String.valueOf(count));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnImgSelect(ActionEvent actionEvent) {
            FileChooser chooser = new FileChooser();
            File file =chooser.showOpenDialog(imgSelectBtn.getScene().getWindow());
            try {
                fileInputStream=new FileInputStream(file);
                image=new Image(fileInputStream);
                img.setImage(image);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }


    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        int id = Integer.parseInt(txtid.getText());
        String name = txtname.getText();
        int qty = Integer.parseInt(txtqty.getText());
        double price = Double.parseDouble(txtprice.getText());
        String description = txtdescription.getText();
        String category = (String) txtcomboBox.getValue();
        Image imgId = img.getImage();
        byte[] blob = model.imagenToByte(imgId);


        if (model.isExists(id)){
            Image image=new Image("/Icon/icons8-cancel-50.png");
            try {
                Notifications notifications=Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("Item is already added");
                notifications.title("Warning");
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            ItemDto itemDto = new ItemDto(id, description, qty, name, price, blob, category);


            try {
                boolean b = model.saveItem(itemDto);

                if(b) {
                    setValueLable();
                    getAllItem();
                    clearFeild();
                    generateNextOrderId();
                    searchTable();

                    Image image=new Image("/Icon/iconsOk.png");
                    try {
                        Notifications notifications=Notifications.create();
                        notifications.graphic(new ImageView(image));
                        notifications.text("Item Saved Successfully");
                        notifications.title("Successfully");
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.TOP_RIGHT);
                        notifications.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    System.out.println("Item saved successfully");
                }else {
                    System.out.println("Item not saved successfully");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        int id = Integer.parseInt(txtid.getText());
        String name = txtname.getText();
        int qty = Integer.parseInt(txtqty.getText());
        double price = Double.parseDouble(txtprice.getText());
        String description = txtdescription.getText();
        String category = (String) txtcomboBox.getValue();
        Image imgId = img.getImage();
        byte[] blob = model.imagenToByte(imgId);

        ItemDto itemDto = new ItemDto(id, description, qty, name, price, blob, category);

        try {
            boolean b = model.updateItem(itemDto);
            if (b) {
                getAllItem();
                clearFeild();
                searchTable();

                Image image=new Image("/Icon/iconsOk.png");
                try {
                    Notifications notifications=Notifications.create();
                    notifications.graphic(new ImageView(image));
                    notifications.text("Item Update Successfully");
                    notifications.title("Successfully");
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.TOP_RIGHT);
                    notifications.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("Item updated successfully");
            }else {
                System.out.println("Item not updated successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        String id = txtid.getText();

        try {
            ItemDto itemDto = model.searchItems(id);
            if (itemDto != null) {
                txtdescription.setText(itemDto.getDescription());
                txtprice.setText(String.valueOf(itemDto.getPrice()));
                txtqty.setText(String.valueOf(itemDto.getQty()));
                txtname.setText(itemDto.getName());

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(itemDto.getImg());

                // Use the input stream to create an Image
                Image image = new Image(byteArrayInputStream);
                img.setImage(image);



                Image image1=new Image("/Icon/iconsOk.png");
                try {
                    Notifications notifications=Notifications.create();
                    notifications.graphic(new ImageView(image1));
                    notifications.text("Item Search Successfully");
                    notifications.title("Successfully");
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.TOP_RIGHT);
                    notifications.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                Image image=new Image("/Icon/icons8-cancel-50.png");
                try {
                    Notifications notifications=Notifications.create();
                    notifications.graphic(new ImageView(image));
                    notifications.text("Customer id does not exist");
                    notifications.title("Not Successfully");
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.TOP_RIGHT);
                    notifications.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void setDeleteBtnAction(Button btn) {
        btn.setOnAction((e) -> {

            InventoryRoot.setEffect(new GaussianBlur());

            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);


            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();


            if (type.orElse(no) == yes) {
                InventoryRoot.setEffect(null);
                int focusedIndex = tblItem.getSelectionModel().getSelectedIndex();
                ItemTm selectedItem = (ItemTm) tblItem.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    int itemId = selectedItem.getItemId();
                    try {
                        boolean b = model.deleteItem(itemId);
                        if (b) {
                                searchTable();
                                Image image=new Image("/Icon/iconsDelete.png");
                                Notifications notifications=Notifications.create();
                                notifications.graphic(new ImageView(image));
                                notifications.text("Item Delete Successfully");
                                notifications.title("Successfully");
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.TOP_RIGHT);
                                notifications.show();

                            System.out.println("delete selected");
                            obList.remove(focusedIndex);
                            getAllItem();
                            setValueLable();
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }else {
                InventoryRoot.setEffect(null);
            }
        });
    }

    public void clearFeild() {
        txtid.clear();
        txtname.clear();
        txtqty.clear();
        txtprice.clear();
        txtdescription.clear();
        img.setImage(null);
        txtcomboBox.getSelectionModel().clearSelection();
    }


    public void txtNameOnAction(ActionEvent actionEvent) {
        txtqty.requestFocus();
    }

    public void txtQtyOnAction(ActionEvent actionEvent) {
        txtprice.requestFocus();
    }

    public void txtPriceOnAction(ActionEvent actionEvent) {
        txtdescription.requestFocus();
    }

    public void txtDescriptionOnAction(ActionEvent actionEvent) {
        txtcomboBox.requestFocus();
    }

    public void searchTable(){
        FilteredList<ItemTm> filteredData = new FilteredList<>(obList, b -> true);

        txtSearchTable.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(itemTm -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String itemId = String.valueOf(itemTm.getItemId());
                String category=itemTm.getCategory().toLowerCase();

                return itemId.contains(lowerCaseFilter)||(category.contains(lowerCaseFilter));
            });
        });

        SortedList<ItemTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblItem.comparatorProperty());
        tblItem.setItems(sortedData);
    }

    public void btnInventoryOrder(ActionEvent actionEvent) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("/view/Inventory/InventoryOrderDetail.fxml"));
        InventoryRoot.getChildren().clear();
        InventoryRoot.getChildren().add(parent);
    }

    public void btnViewOrderDetail(ActionEvent actionEvent) {
    }

    public void btnInventoryDetails(ActionEvent actionEvent) throws IOException {
        Parent parent=FXMLLoader.load(getClass().getResource("/view/Inventory/InventoryForm.fxml"));
        InventoryRoot.getChildren().clear();
        InventoryRoot.getChildren().addAll(parent);
    }
}
