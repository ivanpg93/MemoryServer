/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 * Clase que representa una excepción por parte del jugador.
 * @author Alexandru
 */
public class JugadorException extends Exception {

    /**
     * Constructor de la excepción del jugador
     * @param s mensaje de la excepción.
     */
    public JugadorException(String s) {
        super(s);
    }
    
}
