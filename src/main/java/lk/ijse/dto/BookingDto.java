package lk.ijse.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor

public class BookingDto {
    private int bookingId;
    private String eventType;
    private Date date;
    private String location;
    private int empId;
    private int cusId;
    private int packageId;
}
