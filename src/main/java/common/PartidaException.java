/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 * Clase que representa la excepci�n de una partida.
 * @author Alexandru
 */
public class PartidaException extends Exception {

    /**
     * Constructor de la excepci�n.
     * @param s mensaje que representa la excepci�n.
     */
    public PartidaException(String s) {
        super(s);
    }
    
}
