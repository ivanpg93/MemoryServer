/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

/**
 * Clase que representa una carta del memory
 * @author Alexandru
 */
public class CartaMemory extends Carta {

    private boolean emparejada;
    private boolean girada;

    /**
     * Constructor de la carta mediante un palo y una cara
     * @param palo
     * @param cara 
     */
    public CartaMemory(String palo, String cara) {
        super(palo, cara);
        this.emparejada = false;
    }

    /**
     * Comprueba si la carta ha sido emparejada
     * @return si la carta está emparejada
     */
    public boolean isEmparejada() {
        return emparejada;
    }

    /**
     * Cambia el atributo emparejada de la carta
     * @param emparejada nuevo valor
     */
    public void setMatched(boolean emparejada) {
        this.emparejada = emparejada;
    }

    /**
     * Devuelve verdadero si dos cartas tienen el mismo palo y cara.
     * @param otraCarta
     * @return 
     */
    public boolean isMismaCarta(CartaMemory otraCarta) {
        return (this.getPalo().equals(otraCarta.getPalo())
                && (this.getCara().equals(otraCarta.getCara())));
    }

    /**
     * Comprueba si la carta está girada.
     * @return 
     */
    public boolean isGirada() {
        return girada;
    }

    /**
     * Establece si la carta está girada o no.
     * @param girada 
     */
    public void setGirada(boolean girada) {
        this.girada = girada;
    }

    
}
