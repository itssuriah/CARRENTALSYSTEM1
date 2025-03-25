package main;
import dao.CarLeaseRepositoryImpl;
import entity.Vehicle;
import entity.Customer;
import entity.Lease;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.LeaseNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class MainModule {
   private static final CarLeaseRepositoryImpl carRentalService = new CarLeaseRepositoryImpl();
   private static final Scanner scanner = new Scanner(System.in);
   public static void main(String[] args) {
       while (true) {
           System.out.println("\n===== Car Rental System Menu =====");
           System.out.println("1. Add a New Vehicle");
           System.out.println("2. List Available Vehicles");
           System.out.println("3. Add a Customer");
           System.out.println("4. List Customers");
           System.out.println("5. Create a Lease");
           System.out.println("6. Return a Vehicle");
           System.out.println("7. View Active Leases");
           System.out.println("8. Record a Payment");
           System.out.println("9. Exit");
           System.out.print("Enter your choice: ");
           int choice = scanner.nextInt();
           scanner.nextLine();  // Consume newline
           try {
               switch (choice) {
                   case 1:
                       addVehicle();
                       break;
                   case 2:
                       listAvailableVehicles();
                       break;
                   case 3:
                       addCustomer();
                       break;
                   case 4:
                       listCustomers();
                       break;
                   case 5:
                       createLease();
                       break;
                   case 6:
                       returnVehicle();
                       break;
                   case 7:
                       listActiveLeases();
                       break;
                   case 8:
                       recordPayment();
                       break;
                   case 9:
                       System.out.println("Exiting the Car Rental System. Goodbye!");
                       System.exit(0);
                   default:
                       System.out.println("Invalid choice. Please try again.");
               }
           } catch (SQLException e) {
               System.out.println("Database error: " + e.getMessage());
           } catch (CarNotFoundException | CustomerNotFoundException | LeaseNotFoundException e) {
               System.out.println(e.getMessage());
           }
       }
   }
   private static void addVehicle() throws SQLException {
       System.out.print("Enter Make: ");
       String make = scanner.nextLine();
       System.out.print("Enter Model: ");
       String model = scanner.nextLine();
       System.out.print("Enter Year: ");
       int year = scanner.nextInt();
       System.out.print("Enter Daily Rate: ");
       double dailyRate = scanner.nextDouble();
       scanner.nextLine();  // Consume newline
       System.out.print("Enter Status (available/notAvailable): ");
       String status = scanner.nextLine();
       System.out.print("Enter Passenger Capacity: ");
       int passengerCapacity = scanner.nextInt();
       System.out.print("Enter Engine Capacity: ");
       double engineCapacity = scanner.nextDouble();
       Vehicle vehicle = new Vehicle(0, make, model, year, dailyRate, status, passengerCapacity, engineCapacity);
       carRentalService.addCar(vehicle);
       System.out.println("Vehicle added successfully!");
   }
   private static void listAvailableVehicles() throws SQLException {
       List<Vehicle> vehicles = carRentalService.listAvailableCars();
       if (vehicles.isEmpty()) {
           System.out.println("No available vehicles.");
       } else {
           System.out.println("\nAvailable Vehicles:");
           for (Vehicle vehicle : vehicles) {
               System.out.println(vehicle.getVehicleID() + " - " + vehicle.getMake() + " " + vehicle.getModel());
           }
       }
   }
   private static void addCustomer() throws SQLException {
       System.out.print("Enter First Name: ");
       String firstName = scanner.nextLine();
       System.out.print("Enter Last Name: ");
       String lastName = scanner.nextLine();
       System.out.print("Enter Email: ");
       String email = scanner.nextLine();
       System.out.print("Enter Phone Number: ");
       String phoneNumber = scanner.nextLine();
       Customer customer = new Customer(0, firstName, lastName, email, phoneNumber);
       carRentalService.addCustomer(customer);
       System.out.println("Customer added successfully!");
   }
   private static void listCustomers() throws SQLException {
       List<Customer> customers = carRentalService.listCustomers();
       if (customers.isEmpty()) {
           System.out.println("No customers found.");
       } else {
           System.out.println("\nCustomer List:");
           for (Customer customer : customers) {
               System.out.println(customer.getCustomerID() + " - " + customer.getFirstName() + " " + customer.getLastName());
           }
       }
   }
   private static void createLease() throws SQLException, CarNotFoundException, CustomerNotFoundException {
       System.out.print("Enter Customer ID: ");
       int customerID = scanner.nextInt();
       System.out.print("Enter Vehicle ID: ");
       int vehicleID = scanner.nextInt();
       scanner.nextLine(); // Consume newline
       System.out.print("Enter Start Date (YYYY-MM-DD): ");
       String startDateStr = scanner.nextLine();
       System.out.print("Enter End Date (YYYY-MM-DD): ");
       String endDateStr = scanner.nextLine();
       System.out.print("Enter Lease Type (Daily/Monthly): ");
       String type = scanner.nextLine();
       Lease lease = carRentalService.createLease(customerID, vehicleID, java.sql.Date.valueOf(startDateStr), java.sql.Date.valueOf(endDateStr), type);
       System.out.println("Lease created successfully: Lease ID - " + lease.getLeaseID());
   }
   private static void returnVehicle() throws SQLException, LeaseNotFoundException {
       System.out.print("Enter Lease ID: ");
       int leaseID = scanner.nextInt();
       scanner.nextLine();  // Consume newline
       Lease lease = carRentalService.returnCar(leaseID);
       System.out.println("Vehicle returned successfully. Lease details:");
       System.out.println("Lease ID: " + lease.getLeaseID() + ", Vehicle ID: " + lease.getVehicleID() + ", Customer ID: " + lease.getCustomerID());
   }
   private static void listActiveLeases() throws SQLException {
       List<Lease> leases = carRentalService.listActiveLeases();
       if (leases.isEmpty()) {
           System.out.println("No active leases.");
       } else {
           System.out.println("\nActive Leases:");
           for (Lease lease : leases) {
               System.out.println("Lease ID: " + lease.getLeaseID() + ", Vehicle ID: " + lease.getVehicleID() + ", Customer ID: " + lease.getCustomerID());
           }
       }
   }
   private static void recordPayment() throws SQLException, LeaseNotFoundException {
       System.out.print("Enter Lease ID: ");
       int leaseID = scanner.nextInt();
       System.out.print("Enter Payment Amount: ");
       double amount = scanner.nextDouble();
       scanner.nextLine(); // Consume newline
       carRentalService.recordPayment(leaseID, amount);
       System.out.println("Payment recorded successfully.");
   }
}