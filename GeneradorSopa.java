package uabc.generadorsopa;

import javax.swing.*;
import javax.swing.border.Border;
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

public class GeneradorSopa extends GeneradorSopaAbstracta{
    private JFrame frame;
    private JTextField palabraInput;
    private JTextArea palabrasTextArea;
    private JButton verArchivoButton;
    private ArrayList<String> palabrasList;
    private char[][] sopa;

    public GeneradorSopa() {
        palabrasList = new ArrayList<>();
        initializeUI();
    }
    @Override
    public void initializeUI() {
                frame = new JFrame("Sopa de Letras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        palabraInput = new JTextField();
        panel.add(palabraInput);

        JButton agregarButton = new JButton("Agregar Palabra");
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

        JButton generarButton = new JButton("Generar Sopa de Letras");
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
@Override
    public void actualizarListaElemento() {
        palabrasTextArea.setText("");
        for (String palabra : palabrasList) {
            palabrasTextArea.append(palabra + "\n");
        }
        palabraInput.setText("");
    }
@Override
public void generarSopa() {
    if (palabrasList.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Por favor, agrega al menos una palabra.");
        return;
    }

    sopa = new char[15][15];
    Random r = new Random();

    // Llena la sopa de letras con letras aleatorias.
    for (int i = 0; i < 15; i++) {
        for (int j = 0; j < 15; j++) {
            sopa[i][j] = (char) (r.nextInt(26) + 'a');
        }
    }

    for (String palabra : palabrasList) {
        boolean palabraAgregada = false;
        while (!palabraAgregada) {
            int row = r.nextInt(15);
            int col = r.nextInt(15);

            // Intenta colocar la palabra en todas las direcciones
            int direction = r.nextInt(4); // 0: Vertical, 1: Horizontal, 2: Diagonal hacia arriba, 3: Diagonal hacia abajo

            if (direction == 0 && row + palabra.length() <= 15) { // Vertical hacia abajo
                for (int j = 0; j < palabra.length(); j++) {
                    sopa[row + j][col] = palabra.charAt(j);
                }
                palabraAgregada = true;
            } else if (direction == 1 && col - (palabra.length() - 1) >= 0) { // Horizontal hacia la izquierda
                for (int j = 0; j < palabra.length(); j++) {
                    sopa[row][col - j] = palabra.charAt(j);
                }
                palabraAgregada = true;
            } else if (direction == 2 && row - (palabra.length() - 1) >= 0 && col - (palabra.length() - 1) >= 0) { // Diagonal hacia arriba
                for (int j = 0; j < palabra.length(); j++) {
                    sopa[row - j][col - j] = palabra.charAt(j);
                }
                palabraAgregada = true;
            } else if (direction == 3 && row + palabra.length() <= 15 && col + palabra.length() <= 15) { // Diagonal hacia abajo
                for (int j = 0; j < palabra.length(); j++) {
                    sopa[row + j][col + j] = palabra.charAt(j);
                }
                palabraAgregada = true;
            }
        }
    }
}
@Override
    public void guardarSopa() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("sopa.txt"))) {
        // Escribir el índice de columnas
        writer.write("\t");
        for (int i = 1; i <= 15; i++) {
            writer.write(i + "\t");
        }
        writer.newLine();

        for (int i = 0; i < 15; i++) {
            // Escribir el índice de filas
            writer.write((i+1) + "\t");
            for (int j = 0; j < 15; j++) {
                writer.write(sopa[i][j] + "\t"); // Agregar un tab entre cada letra
            }
            writer.newLine();
        }

        // Aquí es donde escribirías las palabras y sus posiciones.
        writer.write("Palabras Escondidas:\n");
        for (String palabra : palabrasList) {
            writer.write(palabra + "\n");
            buscarYGuardarPalabra(palabra, writer);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void buscarYGuardarPalabra(String palabra, BufferedWriter writer) throws IOException {
    for (int i = 0; i < 15; i++) {
        for (int j = 0; j < 15; j++) {
            if (buscarPalabraEnSopa(palabra, i, j)) {
                writer.write(palabra + ": (" + (i+1) + "," + (j+1) + ")\n");
            }
        }
    }
}

private boolean buscarPalabraEnSopa(String palabra, int row, int col) {
    return buscarPalabraVerticalAbajo(palabra, row, col) ||
           buscarPalabraVerticalArriba(palabra, row, col) ||
           buscarPalabraHorizontalDerecha(palabra, row, col) ||
           buscarPalabraHorizontalIzquierda(palabra, row, col) ||
           buscarPalabraDiagonalDerechaAbajo(palabra, row, col) ||
           buscarPalabraDiagonalDerechaArriba(palabra, row, col) ||
           buscarPalabraDiagonalIzquierdaAbajo(palabra, row, col) ||
           buscarPalabraDiagonalIzquierdaArriba(palabra, row, col);
}

    private boolean buscarPalabraVerticalAbajo(String palabra, int row, int col) {
    if (row + palabra.length() <= 15) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row + i][col] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraVerticalArriba(String palabra, int row, int col) {
    if (row - (palabra.length() - 1) >= 0) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row - i][col] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraHorizontalDerecha(String palabra, int row, int col) {
    if (col + palabra.length() <= 15) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row][col + i] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraHorizontalIzquierda(String palabra, int row, int col) {
    if (col - (palabra.length() - 1) >= 0) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row][col - i] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraDiagonalDerechaAbajo(String palabra, int row, int col) {
    if (row + palabra.length() <= 15 && col + palabra.length() <= 15) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row + i][col + i] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraDiagonalDerechaArriba(String palabra, int row, int col) {
    if (row - (palabra.length() - 1) >= 0 && col + palabra.length() <= 15) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row - i][col + i] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraDiagonalIzquierdaAbajo(String palabra, int row, int col) {
    if (row + palabra.length() <= 15 && col - (palabra.length() - 1) >= 0) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row + i][col - i] != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    return false;
}

private boolean buscarPalabraDiagonalIzquierdaArriba(String palabra, int row, int col) {
    if (row - (palabra.length() - 1) >= 0 && col - (palabra.length() - 1) >= 0) {
        for (int i = 0; i < palabra.length(); i++) {
            if (sopa[row - i][col - i] != palabra.charAt(i)) {
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

            String contenido = new String(Files.readAllBytes(Paths.get("sopa.txt")));
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
                new GeneradorSopa();
            }
        });
    }

    @Override
public void agregarElemento() {
    String nuevaPalabra = palabraInput.getText().toUpperCase();
    if (nuevaPalabra.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Por favor, ingresa una palabra.");
    } else if (palabrasList.contains(nuevaPalabra)) {
        JOptionPane.showMessageDialog(frame, "La palabra ya ha sido agregada.");
    } else if (nuevaPalabra.length() > 10) {
        JOptionPane.showMessageDialog(frame, "La palabra no puede tener más de 10 letras.");
    } else if (!nuevaPalabra.matches("[A-Z]+")) {
        JOptionPane.showMessageDialog(frame, "La palabra solo puede contener letras.");
    } else {
        palabrasList.add(nuevaPalabra);
        actualizarListaElemento();
    }
}

}
