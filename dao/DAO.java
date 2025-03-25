package hex.dao;


import java.util.List;

import hex.entity.Customer;
import hex.entity.Lease;
import hex.entity.Vehicle;
import hex.exception.CarNotFoundException;
import hex.exception.CustomerNotFoundException;


public interface DAO {
  void addCar(Vehicle car);
  void removeCar(int carId);
  List<Vehicle> listAvailableCars();
  List<Vehicle> listRentedCars();
  Vehicle findCarById(int carId) throws CarNotFoundException;
  
  void addCustomer(Customer customer);
  void removeCustomer(int customerId);
  List<Customer> listCustomers();
  Customer findCustomerById(int customerId) throws CustomerNotFoundException;
  
  void recordPayment(Lease lease,double amount);
  Lease createLease(int customerID, int carID, String startDate, String endDate, String type);
}
