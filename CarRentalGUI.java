import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class CarRentalGUI extends JFrame {
    private CarRentalSystem crs;
    private JTable carTable;
    private DefaultTableModel tableModel;

    public CarRentalGUI(CarRentalSystem crs) {
        this.crs = crs;
        setTitle("Car Rental System"); 
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Table Model
        String[] columns = {"Car ID", "Brand", "Model", "Price/Day", "Fuel", "Transmission", "Mileage", "Type", "Seats", "Color", "Reg. No"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(carTable);

        // Buttons
        JButton rentButton = new JButton("Rent Car");
        JButton returnButton = new JButton("Return Car");
        JButton refreshButton = new JButton("Refresh");

        rentButton.addActionListener(e -> rentCar());
        returnButton.addActionListener(e -> returnCar());
        refreshButton.addActionListener(e -> loadAvailableCars());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rentButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAvailableCars();
    }

    private void loadAvailableCars() {
        tableModel.setRowCount(0);
        for (Car car : crs.getCars()) {
            if (car.isAvailable()) {
                tableModel.addRow(new Object[]{
                        car.getCarId(), car.getBrand(), car.getModel(), car.getBasePricePerDay(),
                        car.getFuelType(), car.getTransmission(), car.getMileage(),
                        car.getVehicleType(), car.getSeatingCapacity(), car.getColor(),
                        car.getRegistrationNumber()
                });
            }
        }
    }

    private void rentCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.");
            return;
        }

        String carId = (String) tableModel.getValueAt(selectedRow, 0);
        Car selectedCar = crs.findCarById(carId);
        if (selectedCar == null || !selectedCar.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Selected car is not available.");
            return;
        }

        // Collect customer details
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name == null || name.trim().isEmpty()) return;

        String contact = JOptionPane.showInputDialog(this, "Enter your contact number:");
        if (contact == null || contact.trim().isEmpty()) return;

        String license = JOptionPane.showInputDialog(this, "Enter your license number:");
        if (license == null || license.trim().isEmpty()) return;

        Customer customer = new Customer("CUS" + (crs.getCustomers().size() + 1), name, contact, license);
        crs.addCustomer(customer);

        // Rental dates
        String startDateStr = JOptionPane.showInputDialog(this, "Enter start date (YYYY-MM-DD):");
        String endDateStr = JOptionPane.showInputDialog(this, "Enter return date (YYYY-MM-DD):");
        if (startDateStr == null || endDateStr == null) return;

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            JOptionPane.showMessageDialog(this, "Return date cannot be before start date.");
            return;
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (days == 0) days = 1;

        int insuranceOption = JOptionPane.showConfirmDialog(this, "Add insurance?", "Insurance", JOptionPane.YES_NO_OPTION);
        boolean insurance = (insuranceOption == JOptionPane.YES_OPTION);

        double total = selectedCar.calculatePrice((int) days);
        if (insurance) total += crs.getInsuranceFeePerDay() * days;

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Total price: %.2f\nConfirm booking?", total),
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            crs.rentCar(selectedCar, customer, startDate, endDate, insurance);
            loadAvailableCars();
        }
    }

    private void returnCar() {
        String carId = JOptionPane.showInputDialog(this, "Enter Car ID to return:");
        if (carId == null || carId.trim().isEmpty()) return;

        crs.returnCar(carId);
        loadAvailableCars();
    }
}
