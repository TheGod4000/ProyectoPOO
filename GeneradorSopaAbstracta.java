/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.generadorsopa;

import javax.swing.*;
import java.util.ArrayList;

public abstract class GeneradorSopaAbstracta implements Generador {
    protected JFrame frame;
    protected JTextField palabraInput;
    protected JTextArea palabrasTextArea;
    protected JButton verArchivoButton;
    protected ArrayList<String> palabrasList;
    protected char[][] sopa;

    public GeneradorSopaAbstracta() {
        palabrasList = new ArrayList<>();
        initializeUI();
    }
    public abstract void initializeUI();
     
    @Override
    public abstract void agregarElemento();

    @Override
    public abstract void generarSopa();

    @Override
    public abstract void guardarSopa();

    @Override
    public abstract void verArchivo();
}
