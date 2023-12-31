package lk.ijse.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lk.ijse.db.DbConnection;
import lk.ijse.model.DashboardModel;
import org.controlsfx.control.Notifications;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUserController {

    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    private DashboardModel model=new DashboardModel();
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        boolean b = model.deleteUser(txtUsername.getText(), txtPassword.getText());

        if (b){
            Image image=new Image("/Icon/iconsOk.png");
            try {
                Notifications notifications=Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("User Delete Successfully");
                notifications.title("Successfully");
                notifications.hideAfter(Duration.seconds(3));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Image image = new Image("/Icon/icons8-cancel-50.png");
            try {
                Notifications notifications = Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("User Delete Not Successfully");
                notifications.title("Error");
                notifications.hideAfter(Duration.seconds(3));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        boolean b = model.updateUser(txtUsername.getText(), txtPassword.getText());

        if (b){
            Image image=new Image("/Icon/iconsOk.png");
            try {
                Notifications notifications=Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("User Update Successfully");
                notifications.title("Successfully");
                notifications.hideAfter(Duration.seconds(3));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Image image = new Image("/Icon/icons8-cancel-50.png");
            try {
                Notifications notifications = Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("User Update Not Successfully");
                notifications.title("Error");
                notifications.hideAfter(Duration.seconds(3));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {

        boolean b = model.saveUser(txtUsername.getText(), txtPassword.getText());
        if (b){
            Image image=new Image("/Icon/iconsOk.png");
            try {
                Notifications notifications=Notifications.create();
                notifications.graphic(new ImageView(image));
                notifications.text("User Added Successfully");
                notifications.title("Successfully");
                notifications.hideAfter(Duration.seconds(3));
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
                notifications.text("User Added Not Successfully");
                notifications.title("Error");
                notifications.hideAfter(Duration.seconds(3));
                notifications.position(Pos.TOP_RIGHT);
                notifications.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
