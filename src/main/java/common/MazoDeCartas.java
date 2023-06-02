/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa el mazo de cartas.
 * @author Alexandru
 */
public class MazoDeCartas implements Serializable {

    private static final long serialVersionUID = 1L;
    private int maximoCartas = 13;
    private ArrayList<CartaMemory> mazo;

    int contador = 0;
    
    /**
     * Inicializa el mazo de cartas y genera 13 cartas únicas + sus pares.
     */
    public MazoDeCartas() {
        this.mazo = new ArrayList<>();
        List<String> suits = Carta.getPalosValidos();
        List<String> faceNames = Carta.getCarasValidas();

        for (String suit : suits)
        {
            for (String faceName : faceNames)
            {
                if(contador == maximoCartas) break;
                mazo.add(new CartaMemory(suit,faceName));
                mazo.add(new CartaMemory(suit,faceName));
                contador++;
            }
        }
    }

    /**
     * función que se encarga de randomizar el mazo
     */
    public void mezclar()
    {
        Collections.shuffle(mazo);
    }

    /**
     * Devuelve la lista de cartas
     * @return lista de cartas.
     */
    public List<CartaMemory> getCartas()
    {
        return mazo;
    }

    /**
     * Devuelve el número de cartas-
     * @return obtiene el numero de cartas.
     */
    public int getNumeroCartas()
    {
        return mazo.size();
    }
}
