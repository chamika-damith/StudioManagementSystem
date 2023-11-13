CREATE DATABASE IF NOT EXISTS StudioManagementSystem;


USE StudioManagementSystem;

CREATE TABLE IF NOT EXISTS User(
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(45),
    password VARCHAR(45)
);

CREATE TABLE IF NOT EXISTS Orders(
    orderId INT PRIMARY KEY,
    description VARCHAR(100) NOT NULL ,
    orderDate DATE NOT NULL,
    returnDate DATE,
    userId INT,
    CONSTRAINT FOREIGN KEY(userId) REFERENCES user(userId)
);

ALTER TABLE Orders ADD cusId INT;
ALTER TABLE Orders ADD CONSTRAINT FOREIGN KEY (cusId) REFERENCES customer(cusId) ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS Item(
    itemId INT PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    qty INT NOT NULL,
    name VARCHAR(45)NOT NULL,
    price DOUBLE NOT NULL,
    img longblob,
    category VARCHAR(45)
);


CREATE TABLE IF NOT EXISTS order_detail(
    orderId INT NOT NULL,
    itemId INT NOT NULL,
    CONSTRAINT FOREIGN KEY(orderId) REFERENCES orders(orderId)ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY(itemId) REFERENCES Item(itemId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Supplier(
    supId INT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    contact VARCHAR(45) NOT NULL,
    address VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS Supplier_order(
    supOrderId INT PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    orderDate DATE,
    returnDate DATE,
    category ENUM('Camera', 'Lens', 'Drones', 'Lighting','Accesories'),
    supId INT NOT NULL,
    CONSTRAINT FOREIGN KEY(supId) REFERENCES supplier(supId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS supOrderDetail(
    itemId INT NOT NULL,
    supOrderId INT NOT NULL,
    CONSTRAINT FOREIGN KEY(itemId) REFERENCES Item(itemId) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY(supOrderId) REFERENCES supplier_order(supOrderId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Customer(
    cusId INT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    mobile VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    address VARCHAR(45) NOT NULL
);


CREATE TABLE IF NOT EXISTS Packages (
    packageId INT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    price DOUBLE NOT NULL ,
    type VARCHAR(45) NOT NULL
);


CREATE TABLE IF NOT EXISTS Employee(
    empId INT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    mobile VARCHAR(45) NOT NULL,
    salary DOUBLE NOT NULL,
    email VARCHAR(45) NOT NULL,
    type VARCHAR(45) NOT NULL,
    position ENUM('Admin','Cashier','Manager','Worker')
);

CREATE TABLE IF NOT EXISTS Attendance(
    attId INT PRIMARY KEY,
    status VARCHAR(45) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    empId INT NOT NULL,
    CONSTRAINT FOREIGN KEY(empId) REFERENCES Employee(empId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Booking(
    bookingId INT PRIMARY KEY,
    eventType VARCHAR(45) NOT NULL,
    date DATE NOT NULL,
    location VARCHAR(45) NOT NULL,
    empId INT NOT NULL,
    packageId INT NOT NULL,
    CONSTRAINT FOREIGN KEY(empId) REFERENCES Employee(empId) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (packageId) REFERENCES Packages(packageId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EmpBookingDetail(
    empId INT NOT NULL,
    bookingId INT NOT NULL,
    employeeCount INT NOT NULL,
    CONSTRAINT FOREIGN KEY (empId) REFERENCES Employee(empId) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON UPDATE CASCADE ON DELETE CASCADE
);


show tables;