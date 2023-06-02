/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entidad que representa una partida.
 *
 * @author Alexandru
 */
@Entity
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;

    private boolean partidaFinalizada;

    private int numIntentos;

    private int tiempoRestante;

    private int puntos;

    /*
        0  --> Fácil
        1 --> Medio
        2 --> Difícil
     */
    private int dificultad;

    public Partida() {
    }

    /**
     * Construye una partida según un jugador y la dificultad
     *
     * @param jugador el jugador
     * @param dificultad la dificultad
     */
    public Partida(Jugador jugador, int dificultad) {
        this.jugador = jugador;
        this.partidaFinalizada = false;
        this.dificultad = dificultad;
        this.numIntentos = 0;
        this.puntos = 0;
    }

    /**
     * Obtiene el id de la partida
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Setea el id de la carta;
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el jugador.
     *
     * @return devuelve el jugador.
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Establece el jugador.
     *
     * @param jugador jugador a establecer.
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Devuelve el valor de partidaFinalizada.
     *
     * @return valor de partidaFinalizada.
     */
    public boolean isPartidaFinalizada() {
        return partidaFinalizada;
    }

    /**
     * Establece la partida finalizada.
     *
     * @param partidaFinalizada valor de la partida.
     */
    public void setPartidaFinalizada(boolean partidaFinalizada) {
        this.partidaFinalizada = partidaFinalizada;
    }

    /**
     * Obtiene el valor de la variable dificultad.
     *
     * @return el valor de la dificultad.
     */
    public int getDificultad() {
        return dificultad;
    }

    /**
     * Establece la dificultad
     *
     * @param dificultad la dificultad
     */
    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    /**
     * Obtiene el numero de intentos
     *
     * @return numero de intentos.
     */
    public int getNumIntentos() {
        return numIntentos;
    }

    /**
     * Establece el numero de intentos.
     *
     * @param numIntentos el numero de intentos
     */
    public void setNumIntentos(int numIntentos) {
        this.numIntentos = numIntentos;
    }

    /**
     * Obtiene el tiempo restante
     *
     * @return tiempo restante.
     */
    public int getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Establece el tiempo restante.
     *
     * @param tiempoRestante establece el tiempo restante.
     */
    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * Obtiene los puntos.
     *
     * @return puntos de la partida.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Establece los puntos de la partida.
     *
     * @param puntos puntos de la partida.
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return jugador.getNombre() + " puntuación: " + puntos;
    }

}
