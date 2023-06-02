/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 * Función encargada de las excepciones 
 * @author Alexandru
 */
public class CartaException extends Exception {

    /**
     * Constructor de la excepción
     * @param s mensaje de la excepción
     */
    public CartaException(String s) {
        super(s);
    }
    
}
