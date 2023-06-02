/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import main.Validadors;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author alex
 */
public class Utils {
    // Variables que controlan la reproducci�n de la m�sica
    private static Media media = null;
    private static MediaPlayer player = null;
    
    // Variable que controla si el usuario est� logueado
    public static boolean login = false;
    
    // Variable que controla la dificultad elegida por el usuario para empezar la partida
    public static int dificultad;

    /**
     * *
     * Valida regles de negoci anotades (veure anotacions al BEAN +
     * https://javaee.github.io/tutorial/bean-validation002.html) i controla
     * transacció
     *
     * @param ob
     * @param userTransaction
     * @param em
     * @param log
     * @return
     * @throws Exception
     */
    public static Object persisteixAmbTransaccio(Object ob, UserTransaction userTransaction, EntityManager em, Logger log) throws Exception {
        List<String> errors = Validadors.validaBean(ob);

        if (errors.isEmpty()) {
            try {

                userTransaction.begin();
                em.persist(ob);
                userTransaction.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                String msg = "Error guardando: " + errors.toString();
                log.log(Level.INFO, msg);
                throw new Exception(msg);
            }

        } else {
            String msg = "Errores de validaci�n: " + errors.toString();
            log.log(Level.INFO, msg);
            throw new Exception(msg);
        }

        return ob;
    }
    
    
    /**
     * *
     * Valida regles de negoci anotades (veure anotacions al BEAN +
     * https://javaee.github.io/tutorial/bean-validation002.html) i controla
     * transacció
     *
     * @param ob
     * @param userTransaction
     * @param em
     * @param log
     * @return
     * @throws Exception
     */
    public static Object actualizaAmbTransaccio(Object ob, UserTransaction userTransaction, EntityManager em, Logger log) throws Exception {
        List<String> errors = Validadors.validaBean(ob);

        if (errors.isEmpty()) {
            try {

                userTransaction.begin();
                em.merge(ob);
                userTransaction.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                String msg = "Error guardando: " + errors.toString();
                log.log(Level.INFO, msg);
                throw new Exception(msg);
            }

        } else {
            String msg = "Errores de validaci�n: " + errors.toString();
            log.log(Level.INFO, msg);
            throw new Exception(msg);
        }

        return ob;
    }

    /**
     * Alerta de confirmaci�n al salir de la app
     */
    public static void alertExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Di�logo de confirmaci�n");
        alert.setHeaderText(null);
        alert.setContentText("�Deseas salir del juego?");

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent()) {
            // Si confirmas que deseas salir, se cierra el juego
            if (resultado.get() == ButtonType.OK) {
                Platform.exit();
            }
        }
    }
    
        /**
     * Alerta de Victoria
     * @param puntos puntos a enseñar
     */
    public static void alertVictoria(int puntos) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Has ganado! \n Tu puntuación: " + puntos);
        alert.show();
    }
    
    /**
     * Alerta de aviso de tiempo acabado
     */
    public static void alertTime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("�Tiempo finalizado!");
        alert.show();
    }
    
    /**
     * Alerta de aviso de login
     */
    public static void alertLogin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("�Debes hacer login antes de seguir en la app!");
        alert.showAndWait();
    }

    /**
     * Obtener d�a y hora actual con formato EU
     *
     * @return String
     */
    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());
        return date;
    }
    
    /**
     * Reproducir la m�sica de fondo del juego indefinidamente
     */
    public static void playMusic() {
        
        // Cogemos la ruta del archivo mp3
        var musicResource = Utils.class.getClassLoader().getResource("logica/music/soundtrack.mp3");

        try {
            // Actualizamos el recurso MP3
            media = new Media(musicResource.toURI().toString());

            // Inicializamos el reproductor
            player = new MediaPlayer(media);

            // Configuraciones adicionales del reproductor
            player.setCycleCount(MediaPlayer.INDEFINITE); // Repetir la m�sica de fondo indefinidamente
            player.setVolume(0.25); // Volumen (0.0 - 1.0)
            player.setStartTime(Duration.ZERO); // Iniciar desde el principio
            
            // Reproducir la m�sica
            player.play();

        } catch (URISyntaxException e) {
            System.out.println("ERROR abriendo el fichero: " + musicResource + ":" + e.toString());
        }
    }
    
    /**
     * A partir de segundos, formatear en minutos y segundos
     * @param seconds
     * @return String (minutos:segundos)
     */
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int segundosRestantes = seconds % 60;
        return String.format("%02d:%02d", minutes, segundosRestantes);
    }

    /**
     * A partir de milisegundos, formatear en minutos, segundos y milisegundos
     * @param milliseconds
     * @return String (minutos:segundos:milisegundos)
     */
    public static String formatTimeMillis(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        int millis = milliseconds % 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, millis);
    }
    
}
