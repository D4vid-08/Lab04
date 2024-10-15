package com.mycompany.servicioeps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

class Paciente {
    String nombre;
    int edad;
    String afiliacion; // POS o PC
    String condicion; // embarazo o limitación motriz
    int prioridad; // Nivel de prioridad

    public Paciente(String nombre, int edad, String afiliacion, String condicion) {
        this.nombre = nombre;
        this.edad = edad;
        this.afiliacion = afiliacion;
        this.condicion = condicion;
        this.prioridad = calcularPrioridad();
    }

    private int calcularPrioridad() {
        if (edad < 12 || edad > 65) return 1; // Niños y ancianos
        if (condicion.equalsIgnoreCase("embarazo")) return 1; // Mujeres embarazadas
        if (condicion.equalsIgnoreCase("limitacion motriz")) return 1; // Limitación motriz
        if (afiliacion.equalsIgnoreCase("PC")) return 2; // Plan complementario
        return 3; // Pacientes regulares
    }
}

public class ServicioEPS extends JFrame {
    private Queue<Paciente> colaPacientes = new LinkedList<>();
    private JTextArea areaDeVisualizacion;
    private Timer temporizador;

    public ServicioEPS() {
        setTitle("Sistema de Entrega de Medicamentos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        areaDeVisualizacion = new JTextArea();
        areaDeVisualizacion.setEditable(false);
        add(new JScrollPane(areaDeVisualizacion), BorderLayout.CENTER);

        JPanel panelDeEntrada = new JPanel();
        JTextField campoNombre = new JTextField(10);
        JTextField campoEdad = new JTextField(3);
        JTextField campoAfiliacion = new JTextField(3);
        JTextField campoCondicion = new JTextField(15);
       
        JButton botonAgregar = new JButton("Agregar Paciente");
       
        panelDeEntrada.add(new JLabel("Nombre:"));
        panelDeEntrada.add(campoNombre);
        panelDeEntrada.add(new JLabel("Edad:"));
        panelDeEntrada.add(campoEdad);
        panelDeEntrada.add(new JLabel("Afiliación (POS/PC):"));
        panelDeEntrada.add(campoAfiliacion);
        panelDeEntrada.add(new JLabel("Condición:"));
        panelDeEntrada.add(campoCondicion);
       
        panelDeEntrada.add(botonAgregar);
       
        add(panelDeEntrada, BorderLayout.SOUTH);

        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                int edad = Integer.parseInt(campoEdad.getText());
                String afiliacion = campoAfiliacion.getText();
                String condicion = campoCondicion.getText();

                Paciente paciente = new Paciente(nombre, edad, afiliacion, condicion);
                colaPacientes.offer(paciente);
                actualizarVisualizacion();
                iniciarTemporizador();
            }
        });
       
        temporizador = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                llamarSiguientePaciente();
            }
        });
    }

    private void llamarSiguientePaciente() {
        if (!colaPacientes.isEmpty()) {
            Paciente siguientePaciente = colaPacientes.poll();
            areaDeVisualizacion.append("Llamando a: " + siguientePaciente.nombre + "\n");
            actualizarVisualizacion();
            temporizador.restart(); // Reiniciar el temporizador para el siguiente paciente
        }
    }

    private void actualizarVisualizacion() {
        areaDeVisualizacion.append("Turnos pendientes: " + colaPacientes.size() + "\n");
    }

    private void iniciarTemporizador() {
        if (!temporizador.isRunning()) {
            temporizador.start();
            areaDeVisualizacion.append("El sistema comenzará a llamar pacientes en 5 minutos.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicioEPS sistema = new ServicioEPS();
            sistema.setVisible(true);
        });
    }
}
