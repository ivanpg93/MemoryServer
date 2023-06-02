/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Función que representa una carta.
 *
 * @author Alexandru
 */
public class Carta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String palo;

    private String cara;
    

    public Carta() {
    }

    public Carta(String palo, String cara) {
        this.palo = palo;
        this.cara = cara;
    }


    /**
     * Obtiene el palo de la carta
     *
     * @return palo de la carta
     */
    public String getPalo() {
        return palo;
    }

    /**
     * Establece el palo de la carta
     *
     * @param palo string que representa el palo
     * @throws CartaException si el palo no es válido devuelve una excepción.
     */
    public void setPalo(String palo) throws CartaException {
        palo = palo.toLowerCase();
        if (getPalosValidos().contains(palo)) {
            this.palo = palo;
        } else {
            throw new CartaException(palo + ": Inválido, debe ser una de " + getPalosValidos());
        }

    }

    /**
     * Obtiene la cara de la carta
     *
     * @return cara de la carta
     */
    public String getCara() {
        return cara;
    }

    /**
     * Establece la cara de la carta caras válidas:
     * "2","3","4","5","6","7","8","9","10","jack","queen","king","ace"
     *
     * @param cara string que representa la cara
     */
    public void setCara(String cara) throws CartaException {
        cara = cara.toLowerCase();
        if (getCarasValidas().contains(cara)) {
            this.cara = cara;
        } else {
            throw new CartaException(cara + ": No es válida debe ser una de: " + getCarasValidas());
        }
    }

    /**
     * Obtiene los palos validos
     *
     * @return lista de strings que representa los palos validos.
     */
    public static List<String> getPalosValidos() {
        return Arrays.asList("hearts", "diamonds", "clubs", "spades");
    }

    /**
     * Obtiene las caras validas
     *
     * @return lista de strings con las caras validas.
     */
    public static List<String> getCarasValidas() {
        return Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace");
    }

    /**
     * Convierte la carta a string.
     *
     * @return
     */
    public String toString() {
        return cara + " of " + palo;
    }

    /**
     * Obtiene el color de la carta
     *
     * @return string que representa el color de la carta.
     */
    public String getColour() {
        if (cara.equals("hearts") || palo.equals("diamonds")) {
            return "red";
        } else {
            return "black";
        }
    }

    /**
     * Devuelve el valor de la carta [
     * "2","3","4","5","6","7","8","9","10","jack","queen","king","ace" ] 0 1 2
     * 3 4 .... 11 12 +2
     *
     * @return valor de la carta
     */
    public int getValue() {
        return getCarasValidas().indexOf(cara) + 2;
    }

    /**
     * Obtiene la imagen de la carta
     *
     * @return imagen de la carta
     */
    public byte[] getImage() throws IOException {
        var stream = Carta.class.getClassLoader().getResourceAsStream("images/" + cara + "_of_" + palo + ".png");
        byte[] data = stream.readAllBytes();
        return data;
    }

    /**
     * Obtiene la imagen trasera de la carta
     *
     * @return Imagen de la parte trasera
     */
    public byte[] getBackOfCardImage() throws IOException {
        var stream = Carta.class.getClassLoader().getResourceAsStream("images/back_of_card.png");
        byte[] data = stream.readAllBytes();
        return data;
    }
}
