/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jerc;

/**
 *
 * @author Multipropósito2
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Vehicle {
    String plate;
    String type; // "Bicicleta", "Ciclomotor", "Motocicleta", "Carro"
    long entryTime; // Timestamp for entry time
    int vehicleNumber;

    public Vehicle(String plate, String type, long entryTime, int vehicleNumber) {
        this.plate = plate;
        this.type = type;
        this.entryTime = entryTime;
        this.vehicleNumber = vehicleNumber;
    }

    public long getDuration() {
        return (System.currentTimeMillis() - entryTime) / 60000; // Duration in minutes
    }

    public int calculateCharge() {
        return (int) (switch (type) {
            case "Bicicleta", "Ciclomotor" -> getDuration() * 20;
            case "Motocicleta" -> getDuration() * 30;
            case "Carro" -> getDuration() * 60;
            default -> 0;
        });
    }
}

public class Jerc extends JFrame {
    private List<Vehicle> vehicleList = new ArrayList<>();
    private Stack<Vehicle> twoWheeledVehicles = new Stack<>();
    private Stack<Vehicle> fourWheeledVehicles = new Stack<>();
    private final DefaultTableModel tableModel;

    public Jerc() {
        setTitle("Sistema de Parqueadero Público");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for displaying vehicles
        tableModel = new DefaultTableModel(new String[]{"Número", "Placa", "Tipo", "Hora Ingreso", "Valor a Pagar"}, 0);
        JTable vehicleTable = new JTable(tableModel);
        add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        JTextField plateField = new JTextField(10);
        JTextField typeField = new JTextField(10);
       
        JButton addButton = new JButton("Ingresar Vehículo");
        JButton viewTwoWheelsButton = new JButton("Ver Vehículos de 2 Ruedas");
        JButton viewFourWheelsButton = new JButton("Ver Vehículos de 4 Ruedas");
        JButton removeButton = new JButton("Eliminar Vehículo");
       
        inputPanel.add(new JLabel("Placa:"));
        inputPanel.add(plateField);
        inputPanel.add(new JLabel("Tipo:"));
        inputPanel.add(typeField);
       
        inputPanel.add(addButton);
        inputPanel.add(viewTwoWheelsButton);
        inputPanel.add(viewFourWheelsButton);
        inputPanel.add(removeButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener((ActionEvent e) -> {
            String plate = plateField.getText();
            String type1 = typeField.getText();
            if (!plate.isEmpty() && !type1.isEmpty()) {
                int vehicleNumber = vehicleList.size() + 1; // Auto-incrementing vehicle number
                Vehicle vehicle = new Vehicle(plate, type1, System.currentTimeMillis(), vehicleNumber);
                vehicleList.add(vehicle);
                updateTable();
                if (type1.equalsIgnoreCase("Bicicleta") || type1.equalsIgnoreCase("Ciclomotor")) {
                    twoWheeledVehicles.push(vehicle);
                } else if (type1.equalsIgnoreCase("Motocicleta") || type1.equalsIgnoreCase("Carro")) {
                    fourWheeledVehicles.push(vehicle);
                }
                plateField.setText("");
                typeField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            }
        });

        viewTwoWheelsButton.addActionListener(e -> viewTwoWheeledVehicles());
       
        viewFourWheelsButton.addActionListener(e -> viewFourWheeledVehicles());
       
        removeButton.addActionListener(e -> removeVehicle());
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Vehicle v : vehicleList) {
            tableModel.addRow(new Object[]{
                v.vehicleNumber,
                v.plate,
                v.type,
                v.entryTime,
                v.calculateCharge()
            });
        }
    }

    private void viewTwoWheeledVehicles() {
        StringBuilder sb = new StringBuilder();
        int totalCharge = 0;
       
        for (Vehicle v : twoWheeledVehicles) {
            int charge = v.calculateCharge();
            totalCharge += charge;
            sb.append("Placa: ").append(v.plate).append(", Valor a Pagar: ").append(charge).append("\n");
        }
       
        JOptionPane.showMessageDialog(null, sb.toString() + "\nTotal a Pagar: " + totalCharge + " COP");
    }

    private void viewFourWheeledVehicles() {
        StringBuilder sb = new StringBuilder();
        int totalCharge = 0;

        for (Vehicle v : fourWheeledVehicles) {
            int charge = v.calculateCharge();
            totalCharge += charge;
            sb.append("Placa: ").append(v.plate).append(", Valor a Pagar: ").append(charge).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString() + "\nTotal a Pagar: " + totalCharge + " COP");
    }

    private void removeVehicle() {
        String plateToRemove = JOptionPane.showInputDialog("Ingrese la placa del vehículo a eliminar:");
       
        Vehicle toRemove = null;
       
        for (Vehicle v : vehicleList) {
            if (v.plate.equalsIgnoreCase(plateToRemove)) {
                toRemove = v;
                break;
            }
        }
       
        if (toRemove != null) {
            vehicleList.remove(toRemove);
            if (toRemove.type.equalsIgnoreCase("Bicicleta") || toRemove.type.equalsIgnoreCase("Ciclomotor")) {
                twoWheeledVehicles.remove(toRemove);
            } else if (toRemove.type.equalsIgnoreCase("Motocicleta") || toRemove.type.equalsIgnoreCase("Carro")) {
                fourWheeledVehicles.remove(toRemove);
            }
            updateTable();
            JOptionPane.showMessageDialog(null, "Vehículo eliminado.");
        } else {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jerc system = new Jerc();
            system.setVisible(true);
        });
    }
}
