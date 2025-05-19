import java.time.LocalDate;
import javax.swing.SwingUtilities;
import java.util.*;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;
    private double mileage;
    private String fuelType;
    private String transmission;
    private int seatingCapacity;
    private String color;
    private String vehicleType;
    private String registrationNumber;

    public Car(String carId, String brand, String model, double basePricePerDay,
               double mileage, String fuelType, String transmission, int seatingCapacity,
               String color, String vehicleType, String registrationNumber) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.color = color;
        this.vehicleType = vehicleType;
        this.registrationNumber = registrationNumber;
        this.isAvailable = true;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    public double calculatePrice(int rentalDays) { return basePricePerDay * rentalDays; }
    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }
    public double getMileage() { return mileage; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public String getColor() { return color; }
    public String getVehicleType() { return vehicleType; }
    public String getRegistrationNumber() { return registrationNumber; }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public void displayDetails() {
        System.out.printf("%s - %s %s | %.2f/day | %s | %s | %dkm/l | %s | %d-seater | %s | Reg#: %s\n",
                carId, brand, model, basePricePerDay, fuelType, transmission,
                (int) mileage, vehicleType, seatingCapacity, color, registrationNumber);
    }
}

class Customer {
    private String customerId;
    private String name;
    private String contact;
    private String licenseNumber;

    public Customer(String customerId, String name, String contact, String licenseNumber) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
        this.licenseNumber = licenseNumber;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getLicenseNumber() { return licenseNumber; }
}

class Rental {
    private Car car;
    private Customer customer;
    private LocalDate bookingDate;
    private LocalDate returnDate;
    private boolean insurance;

    public Rental(Car car, Customer customer, LocalDate bookingDate, LocalDate returnDate, boolean insurance) {
        this.car = car;
        this.customer = customer;
        this.bookingDate = bookingDate;
        this.returnDate = returnDate;
        this.insurance = insurance;
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public long getRentalDays() { return java.time.temporal.ChronoUnit.DAYS.between(bookingDate, returnDate); }
    public boolean isInsuranceOpted() { return insurance; }
}

class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();
    private final double insuranceFeePerDay = 10.0;

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    public void rentCar(Car car, Customer customer, LocalDate bookingDate, LocalDate returnDate, boolean insurance) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, bookingDate, returnDate, insurance));
            System.out.println("Car rented successfully.");
        } else {
            System.out.println("Car not available.");
        }
    }

    public void returnCar(String carId) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(carId)) {
                rental.getCar().returnCar();
                System.out.println("Car returned by " + rental.getCustomer().getName());
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car rental not found.");
        }
    }

    public void displayAvailableCars() {
        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                car.displayDetails();
            }
        }
    }

    // ===== Utility Methods =====
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public Car getCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }

    public Customer getCustomerById(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public List<Rental> getAllRentals() {
        return rentals;
    }

    public double calculateTotalCost(Car car, long days, boolean insurance) {
        double total = car.calculatePrice((int) days);
        if (insurance) {
            total += insuranceFeePerDay * days;
        }
        return total;
    }

    public double getInsuranceFeePerDay() {
        return insuranceFeePerDay;
    }

    public List<Car> getAllCars() { return cars; }
    public List<Customer> getAllCustomers() { return customers; }

    
    public List<Car> getCars() {
        return cars;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Car findCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem crs = new CarRentalSystem();

    Car[] carArray = {
    new Car("C001", "Toyota", "Camry", 4500.0, 15.0, "Petrol", "Automatic", 5, "White", "Sedan", "UP32-AB1234"),
    new Car("C002", "Honda", "Civic", 4300.0, 14.0, "Diesel", "Manual", 5, "Black", "Sedan", "UP32-CD5678"),
    new Car("C003", "Mahindra", "Thar", 5000.0, 10.0, "Diesel", "Manual", 4, "Red", "SUV", "UP32-EF9012"),
    new Car("C004", "Hyundai", "Creta", 3800.0, 13.0, "Petrol", "Automatic", 5, "Blue", "SUV", "UP32-GH3456"),
    new Car("C005", "Tata", "Nexon", 3200.0, 17.0, "Petrol", "Manual", 5, "Silver", "Compact SUV", "UP32-IJ7890"),
    new Car("C006", "Maruti", "Swift", 2000.0, 20.0, "Petrol", "Manual", 5, "Grey", "Hatchback", "UP32-KL1234"),
    new Car("C007", "Kia", "Seltos", 4000.0, 14.5, "Diesel", "Automatic", 5, "White", "SUV", "UP32-MN5678"),
    new Car("C008", "Skoda", "Superb", 4700.0, 12.0, "Petrol", "Automatic", 5, "Black", "Sedan", "UP32-OP9012"),
    new Car("C009", "Ford", "EcoSport", 3600.0, 13.5, "Petrol", "Manual", 5, "Red", "SUV", "UP32-QR3456"),
    new Car("C010", "Jeep", "Compass", 5500.0, 11.0, "Diesel", "Automatic", 5, "Green", "SUV", "UP32-ST7890"),
    new Car("C011", "Volkswagen", "Polo", 2500.0, 19.0, "Petrol", "Manual", 5, "Blue", "Hatchback", "UP32-UV1234"),
    new Car("C012", "BMW", "X1", 9500.0, 9.0, "Petrol", "Automatic", 5, "White", "Luxury SUV", "UP32-WX5678"),
    new Car("C013", "Mercedes", "A-Class", 10500.0, 8.5, "Diesel", "Automatic", 5, "Black", "Luxury Sedan", "UP32-YZ9012"),
    new Car("C014", "Audi", "Q3", 9800.0, 9.5, "Petrol", "Automatic", 5,"Grey", "Luxury SUV", "UP32-AB3456"),
    new Car("C015", "Nissan", "Magnite", 3100.0, 17.5, "Petrol", "Manual", 5, "Brown", "Compact SUV", "UP32-CD7890"),
    new Car("C016", "Renault", "Kiger", 3000.0, 18.0, "Petrol", "Manual", 5, "Orange", "Compact SUV", "UP32-EF1234"),
    new Car("C017", "MG", "Hector", 4200.0, 13.0, "Diesel", "Automatic", 5, "White", "SUV", "UP32-GH5678"),
    new Car("C018", "Honda", "City", 3900.0, 16.0, "Petrol", "Automatic", 5, "Silver", "Sedan", "UP32-IJ9012"),
    new Car("C019", "Toyota", "Innova", 4800.0, 12.0, "Diesel", "Manual", 7, "Beige", "MPV", "UP32-KL3456"),
    new Car("C020", "Hyundai", "i20", 2700.0, 19.5, "Petrol", "Manual", 5, "Red", "Hatchback", "UP32-MN7890"),
    new Car("C021", "Suzuki", "Baleno", 2600.0, 20.0, "Petrol", "Manual", 5, "Blue", "Hatchback", "UP32-OP1234"),
    new Car("C022", "Tata", "Tiago", 2400.0, 21.0, "Petrol", "Manual", 5, "Grey", "Hatchback", "UP32-QR5678"),
    new Car("C023", "Ford", "Figo", 2500.0, 19.0, "Diesel", "Manual", 5, "Yellow", "Hatchback", "UP32-ST9012")
};


        for (Car car : carArray) {
            crs.addCar(car);
        }

        // GUI or CLI interface can be initialized here
        // crs.menu(); // or new CarRentalGUI(crs);

        // Launch GUI
        SwingUtilities.invokeLater(() -> new CarRentalGUI(crs));
    }
}