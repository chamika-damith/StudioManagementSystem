package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.OrderViewDto;
import lk.ijse.dto.ViewBookingDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class viewBookingModel {
    public List<ViewBookingDto> getAllBooking() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT b.bookingId,b.status,c.name,b.location,c.email,c.mobile FROM booking b JOIN customer c ON b.custId = c.cusId";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<ViewBookingDto> dto=new ArrayList<>();

        while (resultSet.next()) {
            dto.add(new ViewBookingDto(
                    resultSet.getInt("bookingId"),
                    resultSet.getString("name"),
                    resultSet.getString("location"),
                    resultSet.getString("email"),
                    resultSet.getString("mobile"),
                    resultSet.getBoolean("status")
            ));
        }
        return dto;
    }

    public List<ViewBookingDto> getTodayBooking(Date date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT b.bookingId,b.status,c.name,b.location,c.email,c.mobile FROM booking b JOIN customer c ON b.custId = c.cusId WHERE date=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setDate(1,date);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<ViewBookingDto> dto=new ArrayList<>();

        while (resultSet.next()) {
            dto.add(new ViewBookingDto(
                    resultSet.getInt("bookingId"),
                    resultSet.getString("name"),
                    resultSet.getString("location"),
                    resultSet.getString("email"),
                    resultSet.getString("mobile"),
                    resultSet.getBoolean("status")
            ));
        }
        return dto;
    }
}
