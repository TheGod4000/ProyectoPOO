/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.generadorsopa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class GeneradorSopaNum extends GeneradorSopaAbstracta{
    private JFrame frame;
    private JTextField palabraInput;
    private JTextArea palabrasTextArea;
    private JButton verArchivoButton;
    private ArrayList<String> palabrasList;
    private int[][] sopa;

    public GeneradorSopaNum() {
        palabrasList = new ArrayList<>();
        initializeUI();
    }

@Override
    public void actualizarListaElemento() {
        palabrasTextArea.setText("");
        for (String numero : palabrasList) {
            palabrasTextArea.append(numero + "\n");
        }
        palabraInput.setText("");
    }
@Override
    public void generarSopa() {
        if (palabrasList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, agrega al menos un número.");
            return;
        }

        sopa = new int[15][15];
        Random r = new Random();

        // Llena la sopa de números aleatorios.
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                sopa[i][j] = r.nextInt(10); // Cambia el rango si deseas números más grandes o pequeños
            }
        }

        for (String numero : palabrasList) {
            boolean numeroAgregado = false;
            while (!numeroAgregado) {
                int row = r.nextInt(15);
                int col = r.nextInt(15);

                int direction = r.nextInt(4); // 0: Vertical, 1: Horizontal, 2: Diagonal hacia arriba, 3: Diagonal hacia abajo

                if (direction == 0 && row + numero.length() <= 15) { // Vertical hacia abajo
                    for (int j = 0; j < numero.length(); j++) {
                        sopa[row + j][col] = Character.getNumericValue(numero.charAt(j));
                    }
                    numeroAgregado = true;
                } else if (direction == 1 && col - (numero.length() - 1) >= 0) { // Horizontal hacia la izquierda
                    for (int j = 0; j < numero.length(); j++) {
                        sopa[row][col - j] = Character.getNumericValue(numero.charAt(j));
                    }
                    numeroAgregado = true;
                } else if (direction == 2 && row - (numero.length() - 1) >= 0 && col - (numero.length() - 1) >= 0) { // Diagonal hacia arriba
                    for (int j = 0; j < numero.length(); j++) {
                        sopa[row - j][col - j] = Character.getNumericValue(numero.charAt(j));
                    }
                    numeroAgregado = true;
                } else if (direction == 3 && row + numero.length() <= 15 && col + numero.length() <= 15) { // Diagonal hacia abajo
                    for (int j = 0; j < numero.length(); j++) {
                        sopa[row + j][col + j] = Character.getNumericValue(numero.charAt(j));
                    }
                    numeroAgregado = true;
                }
            }
        }
    }
@Override
    public void guardarSopa() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sopa_numerica.txt"))) {
            // Escribir el índice de columnas
            writer.write("\t");
            for (char c = 'A'; c < 'A' + 15; c++) {
                writer.write(c + "\t");
            }
            writer.newLine();

            for (int i = 0; i < 15; i++) {
                // Escribir el índice de filas
                writer.write((i + 1) + "\t");
                for (int j = 0; j < 15; j++) {
                    writer.write(sopa[i][j] + "\t"); // Agregar un tab entre cada número
                }
                writer.newLine();
            }

            // Aquí es donde escribirías los números y sus posiciones.
            writer.write("Números Escondidos:\n");
            for (String numero : palabrasList) {
                writer.write(numero + "\n");
                buscarYGuardarNumero(numero, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buscarYGuardarNumero(String numero, BufferedWriter writer) throws IOException {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (buscarNumeroEnSopa(numero, i, j)) {
                    writer.write(numero + ": (" + (char) ('A' + j) + "," + (i + 1) + ")\n");
                }
            }
        }
    }

    private boolean buscarNumeroEnSopa(String numero, int row, int col) {
        return buscarNumeroVerticalAbajo(numero, row, col) ||
                buscarNumeroVerticalArriba(numero, row, col) ||
                buscarNumeroHorizontalDerecha(numero, row, col) ||
                buscarNumeroHorizontalIzquierda(numero, row, col) ||
                buscarNumeroDiagonalDerechaAbajo(numero, row, col) ||
                buscarNumeroDiagonalDerechaArriba(numero, row, col) ||
                buscarNumeroDiagonalIzquierdaAbajo(numero, row, col) ||
                buscarNumeroDiagonalIzquierdaArriba(numero, row, col);
    }

    private boolean buscarNumeroVerticalAbajo(String numero, int row, int col) {
        if (row + numero.length() <= 15) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row + i][col] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroVerticalArriba(String numero, int row, int col) {
        if (row - (numero.length() - 1) >= 0) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row - i][col] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroHorizontalDerecha(String numero, int row, int col) {
        if (col + numero.length() <= 15) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row][col + i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroHorizontalIzquierda(String numero, int row, int col) {
        if (col - (numero.length() - 1) >= 0) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row][col - i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroDiagonalDerechaAbajo(String numero, int row, int col) {
        if (row + numero.length() <= 15 && col + numero.length() <= 15) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row + i][col + i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroDiagonalDerechaArriba(String numero, int row, int col) {
        if (row - (numero.length() - 1) >= 0 && col + numero.length() <= 15) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row - i][col + i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroDiagonalIzquierdaAbajo(String numero, int row, int col) {
        if (row + numero.length() <= 15 && col - (numero.length() - 1) >= 0) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row + i][col - i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean buscarNumeroDiagonalIzquierdaArriba(String numero, int row, int col) {
        if (row - (numero.length() - 1) >= 0 && col - (numero.length() - 1) >= 0) {
            for (int i = 0; i < numero.length(); i++) {
                if (sopa[row - i][col - i] != Character.getNumericValue(numero.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
@Override
    public void verArchivo() {
        try {
            JFrame verArchivoFrame = new JFrame("Contenido del Archivo");
            verArchivoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            verArchivoFrame.setSize(400, 400);

            JTextArea contenidoArchivo = new JTextArea(15, 30);
            contenidoArchivo.setEditable(false);

            String contenido = new String(Files.readAllBytes(Paths.get("sopa_numerica.txt")));
            contenidoArchivo.setText(contenido);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(contenidoArchivo, BorderLayout.CENTER);

            verArchivoFrame.add(panel);
            verArchivoFrame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GeneradorSopaNum();
            }
        });
    }

    @Override
public void agregarElemento() {
       String nuevoNumero = palabraInput.getText();
        if (nuevoNumero.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, ingresa un número.");
        } else if (palabrasList.contains(nuevoNumero)) {
            JOptionPane.showMessageDialog(frame, "El número ya ha sido agregado.");
        } else if (nuevoNumero.length() > 10) {
            JOptionPane.showMessageDialog(frame, "El número no puede tener más de 10 dígitos.");
        } else if (!nuevoNumero.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(frame, "Ingresa solo números.");
        } else {
            palabrasList.add(nuevoNumero);
            actualizarListaElemento();
        }
}

    @Override
    public void initializeUI() {
        frame = new JFrame("Sopa de Numeros");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        palabraInput = new JTextField();
        panel.add(palabraInput);

        JButton agregarButton = new JButton("Agregar Numeros");
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarElemento();
            }
        });
        panel.add(agregarButton);

        palabrasTextArea = new JTextArea(5, 20);
        palabrasTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(palabrasTextArea);
        panel.add(scrollPane);

        verArchivoButton = new JButton("Ver Archivo Guardado");
        verArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verArchivo();
            }
        });
        panel.add(verArchivoButton);

        JButton generarButton = new JButton("Generar Sopa de Numeros");
        generarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarSopa();
                guardarSopa();
            }
        });
        panel.add(generarButton);

        frame.add(panel);
        frame.setVisible(true);
    }

}

