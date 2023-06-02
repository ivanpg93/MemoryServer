/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Entidad que representa al jugador.
 * @author Alexandru
 */
@Entity
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "El nombre del jugador no puede estar vacío.")
    @Size(min = 3, max = 10, message = "La longitud ha de estar entre 3 y 10 caracteres")
    private String nombre;

    @Email(message = "El email no es válido")
    private String email;
    
    @OneToMany(mappedBy = "jugador", fetch = FetchType.EAGER)
    private List<Partida> partidas;
    
    public Jugador() {
    }

    /**
     * Crea un jugador con un nombre y email.
     * @param nombre 
     * @param email 
     */
    public Jugador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    @Override
    public String toString() {
        return "Jugador{" + "id=" + id + ", nombre=" + nombre + ", email=" + email + ", partidas=" + partidas + '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
}
