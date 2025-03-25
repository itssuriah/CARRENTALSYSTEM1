package hex.dao;

 

 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import hex.entity.Customer;
import hex.entity.Lease;
import hex.entity.Vehicle;
import hex.exception.CarNotFoundException;
import hex.exception.CustomerNotFoundException;
import hex.util.DBUtil;
 
public class DAO_IMP implements DAO {
    private Connection conn;
    
    public DAO_IMP() throws SQLException{
    	conn=DBUtil.getConnection();
    }
 
    // Car Management
    @Override
    public void addCar(Vehicle car) {
        try {
            String query = "INSERT INTO vehicle (make, model, year, dailyRate, c_status, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getDailyRate());
            stmt.setString(5, car.getStatus());
            stmt.setInt(6, car.getPassengerCapacity());
            stmt.setDouble(7, car.getEngineCapacity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void removeCar(int carID) {
        try {
            String query = "DELETE FROM vehicle WHERE vehicle_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, carID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public List<Vehicle> listAvailableCars() {
        List<Vehicle> cars = new ArrayList<>();
        try {
            String query = "SELECT * FROM vehicle WHERE c_status = 'available'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                cars.add(new Vehicle(rs.getInt("vehicle_id"), rs.getString("make"), rs.getString("model"), rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("c_status"), rs.getInt("passengerCapacity"), rs.getInt("engineCapacity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
 
    @Override
    public List<Vehicle> listRentedCars() {
        List<Vehicle> cars = new ArrayList<>();
        try {
            String query = "SELECT * FROM vehicle WHERE c_status = 'notAvailable'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                cars.add(new Vehicle(rs.getInt("vehicle_id"), rs.getString("make"), rs.getString("model"), rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("c_status"), rs.getInt("passengerCapacity"), rs.getInt("engineCapacity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
 
    @Override
    public Vehicle findCarById(int carID) throws CarNotFoundException {
        try {
            String query = "SELECT * FROM vehicle WHERE vehicle_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, carID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(rs.getInt("vehicle_id"), rs.getString("make"), rs.getString("model"), rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("c_status"), rs.getInt("passengerCapacity"), rs.getInt("engineCapacity"));
            } else {
                throw new CarNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Customer Management
    @Override
    public void addCustomer(Customer customer) {
        try {
            String query = "INSERT INTO customers (f_name, l_name, email, ph_num) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void removeCustomer(int customerID) {
        try {
            String query = "DELETE FROM customers WHERE customerId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            String query = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customerId"), rs.getString("f_name"), rs.getString("l_name"), rs.getString("email"), rs.getString("ph_num")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
 
    @Override
    public Customer findCustomerById(int customerID) throws CustomerNotFoundException {
        try {
            String query = "SELECT * FROM customers WHERE customerId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("customerId"), rs.getString("f_name"), rs.getString("l_name"), rs.getString("email"), rs.getString("ph_num"));
            } else {
                throw new CustomerNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Lease Management
    @Override
    public Lease createLease(int customerID, int carID, String startDate, String endDate, String type) {
        Lease lease = null;
        try {
            String query = "INSERT INTO lease (customer_id, vehicle_id, s_date, e_date, l_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, customerID);
            stmt.setInt(2, carID);
            stmt.setString(3, startDate);
            stmt.setString(4, endDate);
            stmt.setString(5, type);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lease = new Lease();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lease;
    }
    
    @Override
    public void recordPayment(Lease lease, double amount) {
        try {
            String query = "INSERT INTO payment (lease_id, payDate, amount) VALUES (?, NOW(), ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, lease.getLeaseID());
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
