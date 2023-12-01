/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.generadorsopa;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GeneradorSopaAbstracta generadorSopa = null;
                String[] opciones = {"Sopa de Letras", "Sopa de Números"};
                int seleccion = JOptionPane.showOptionDialog(null, "¿Qué tipo de sopa deseas generar?", "Seleccionar Tipo de Sopa", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
                if (seleccion == 0) {
                    generadorSopa = new GeneradorSopa();
                } else if (seleccion == 1) {
                    generadorSopa = new GeneradorSopaNum();
                }
            }
        });
    }
}
