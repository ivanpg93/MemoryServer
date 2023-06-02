package main;

import common.CartaMemory;
import common.IJuego;
import common.Jugador;
import common.JugadorException;
import common.MazoDeCartas;
import common.Partida;
import common.PartidaException;
import common.Utils;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 * *
 * Classe Stateful, que mant� l'estat de les dades entre diverses crides als
 * seus m�todes.
 *
 * @author manel
 */
@Stateful
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value = TransactionManagementType.BEAN) // o b� �s el contenidor el que gestiona les transaccions a la BBDD o b� �s el programador/a manualment
//@TransactionAttribute(TransactionAttributeType.REQUIRED) //https://gerardo.dev/ejb-basics.html
public class JuegoEJB implements IJuego {

    @Resource
    private SessionContext sessionContext;

    // tenim acc�s a la funcionalitat "injectada" al BEAN
    @Resource
    private EJBContext ejbContext;

    // Si utilitzem el entitymanager al llarg de m// Si utilitzem el entitymanager al llarg de m�s d'un m�tode, 
    // la transacci� es pot extendre al llarg de tot el BEAN i passar de m�tode a m�tode
    // Aquest par�metre �s necess�ri per a un TransactionManagementType = CONTAINER
    // Per defecte es fa commit de la transacci�, si s'ha fet un persist, al finalitzar cada m�tode.
    // Es fa rollback si abans de sortir del m�tode, es produeix una excepci�
    @PersistenceContext(unitName = "Exemple1PersistenceUnit", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Resource
    private UserTransaction userTransaction;

    private String emailUsuario = null;
    private Partida partidaActual = null;
    private boolean primerVolteo = true;
    private boolean victoria = false;

    private MazoDeCartas mazo;
    private Timer timer;

    private CartaMemory carta1;
    private CartaMemory carta2;

    private int tiempoMaximo;

    // Injecci� d'un EJB local. En aquest cas no cal fer lookup.
    @EJB
    AppSingletonEJB singleton;

    Queue<String> mensajes;

    private static final Logger log = Logger.getLogger(JuegoEJB.class.getName());

    @PostConstruct
    public void init() {

        log.log(Level.INFO, "Inicializando JugadorEJB: client={0} ; emailUsuario={1}; singletonEJB uptime={2}", new Object[]{sessionContext.getCallerPrincipal().getName(), this.emailUsuario, this.singleton.getUptimeUTC().toString()});

        try {

            mensajes = new LinkedList<>();

            Principal p = ejbContext.getCallerPrincipal();

            log.log(Level.INFO, "Usuario remoto: {0}", p.getName());
            log.log(Level.INFO, "Hash: {0}", p.hashCode());

        } catch (IllegalStateException ex) {
            log.log(Level.INFO, "ERROR conectando:  {0} ", new Object[]{ex.toString()});
        }
    }

    @Override
    public Jugador getSesion(Jugador j) throws JugadorException {
        if ((j.getEmail() == null || j.getEmail().isBlank() || j.getEmail().isEmpty())
                || (j.getNombre() == null || j.getNombre().isBlank() || j.getNombre().isEmpty())) {
            String msg = "El formato del nombre o email no es v�lido";
            log.log(Level.WARNING, msg);
            throw new JugadorException(msg);
        }

        Jugador jugador = null;
        try {
            String consulta = "SELECT j FROM Jugador j WHERE j.email = :email AND j.nombre = :nombre";
            TypedQuery<Jugador> query = em.createQuery(consulta, Jugador.class);
            jugador = query.setParameter("email", j.getEmail()).setParameter("nombre", j.getNombre()).getSingleResult();

        } catch (Exception ex) {
            throw new JugadorException(ex.getMessage());
        }

        if (jugador == null) {
            String msg = "El jugador no existe.";
            log.log(Level.WARNING, msg);
            throw new JugadorException(msg);
        }

        emailUsuario = jugador.getEmail();
        log.log(Level.INFO, "Jugador obtenido: " + jugador.getEmail());
        return jugador;
    }

    @Remove
    @Override
    public void cerrarSesion() {
        log.log(Level.INFO, "Sesi�n finalizada: " + this.emailUsuario);
    }

    @Override
    public Jugador registrarUsuario(Jugador jugador) throws Exception {

        if ((jugador.getEmail() == null || jugador.getEmail().isBlank() || jugador.getEmail().isEmpty())
                || (jugador.getNombre() == null || jugador.getNombre().isBlank() || jugador.getNombre().isEmpty())) {
            String msg = "El formato del nombre o email no es v�lido";
            log.log(Level.WARNING, msg);
            throw new JugadorException(msg);
        }

        Jugador j = null;
        try {
            String consulta = "SELECT j FROM Jugador j WHERE j.email = :email";
            TypedQuery<Jugador> query = em.createQuery(consulta, Jugador.class);
            j = query.setParameter("email", jugador.getEmail())
                    .getSingleResult();
        } catch (Exception ex) {
        }

        if (j != null) {
            String msg = "El usuario ya existe";
            log.log(Level.WARNING, msg);
            throw new JugadorException(msg);
        }

        j = (Jugador) Utils.persisteixAmbTransaccio(jugador, userTransaction, em, log);

        return j;
    }

    @Override
    public Partida terminarPartida() throws Exception {

        if (partidaActual == null) {
            throw new PartidaException("Esta partida no es la misma que la actual");
        }

        partidaActual.setPuntos(calcularPuntos());
        Partida p = (Partida) Utils.actualizaAmbTransaccio(partidaActual, userTransaction, em, log);

        partidaActual = null;
        timer.cancel();
        return p;
    }

    @Override
    public Partida empezarPartida(Partida partida) throws PartidaException {
        Partida p = null;
        partidaActual = null;
        primerVolteo = true;
        victoria = false;

        switch (partida.getDificultad()) {
            case 0:
                tiempoMaximo = 300;
                partida.setTiempoRestante(tiempoMaximo);
                break;
            case 1:
                tiempoMaximo = 200;
                partida.setTiempoRestante(tiempoMaximo);
                break;
            case 2:
                tiempoMaximo = 100;
                partida.setTiempoRestante(tiempoMaximo);
                break;
            default:
                throw new AssertionError();
        }
        try {
            p = (Partida) Utils.persisteixAmbTransaccio(partida, userTransaction, em, log);

        } catch (Exception ex) {
            String msg = "Error al empezar la partida: " + ex.getMessage();
            log.log(Level.WARNING, msg);
            throw new PartidaException(msg);
        }
        partidaActual = p;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    actualizarTiempoPartida();
                } catch (PartidaException ex) {
                    log.log(Level.WARNING, ex.getMessage());
                }
            }
        }, 1000, 1000);
        return p;
    }

    @Override
    public List<Partida> verHistorial(int idJugador) {
        TypedQuery<Partida> query = em.createQuery("SELECT p FROM Partida p WHERE p.jugador.id = :idjugador", Partida.class);
        query.setParameter("idjugador", idJugador);
        return query.getResultList();
    }

    @Override
    public MazoDeCartas obtenerMazoMezclado() {
        mazo = new MazoDeCartas();
        mazo.mezclar();
        return mazo;
    }

    @Override
    public boolean cartasConciden(CartaMemory carta1, CartaMemory carta2, int indiceCarta1, int indiceCarta2) {
        if (carta1 == null || carta2 == null) {
            return false;
        }
        boolean coinciden = carta1.isMismaCarta(carta2);
        if (coinciden) {
            mazo.getCartas().get(indiceCarta1).setMatched(true);
            mazo.getCartas().get(indiceCarta2).setMatched(true);
            carta1.setMatched(true);
            carta2.setMatched(true);

        }
        return coinciden;
    }

    @Override
    public int sumarIntentos() throws Exception {
        if (partidaActual == null) {
            throw new PartidaException("NO existe partida actual");
        }
        partidaActual.setNumIntentos(partidaActual.getNumIntentos() + 1);
        Utils.persisteixAmbTransaccio(partidaActual, userTransaction, em, log);

        return partidaActual.getNumIntentos();
    }

    @Override
    public void voltearCarta() {

        primerVolteo = !primerVolteo;
    }

    @Override
    public List<Partida> getHallOfGame() throws Exception {
        String jpql = "SELECT p FROM Partida p";

        TypedQuery<Partida> query = em.createQuery(jpql, Partida.class);
        List<Partida> resultList = query.getResultList();

        return partidaSinDuplicados(resultList);
    }

    @Override
    public List<Partida> getHallOfGame(int dificultad) throws Exception {
        String jpql = "SELECT p FROM Partida p WHERE p.dificultad = :dificultad ";

        TypedQuery<Partida> query = em.createQuery(jpql, Partida.class);
        query.setParameter("dificultad", dificultad);
        List<Partida> resultList = query.getResultList();

        
        return partidaSinDuplicados(resultList);

    }

    /**
     * Elimina los duplicados del hall of fame
     * @param partidaConDuplicado lista con duplicados
     * @return lista sin duplicados
     */
    private List<Partida> partidaSinDuplicados(List<Partida> partidaConDuplicado) {
        partidaConDuplicado.sort((partida1, partida2) -> partida2.getPuntos() - partida1.getPuntos());

        Set<Long> jugadorIds = new HashSet<>();

        List<Partida> partidaListSinDuplicados = new ArrayList<>();

        for (Partida partida : partidaConDuplicado) {
            Long idJugador = partida.getJugador().getId();
            if (!jugadorIds.contains(idJugador)) {
                partidaListSinDuplicados.add(partida);
                jugadorIds.add(idJugador);
            }
        }
        return partidaListSinDuplicados;
    }

    private int calcularPuntos() {
        int puntos = 0;
        int intentos = partidaActual.getNumIntentos();
        int segundos = partidaActual.getTiempoRestante();

        int maxTiempo = 300;
        switch (partidaActual.getDificultad()) {
            case 0:
                maxTiempo = 300;
                break;
            case 1:
                maxTiempo = 200;
                break;
            case 2:
                maxTiempo = 100;
                break;
            default:
                throw new AssertionError();
        }

        puntos = (80 - intentos) * (maxTiempo - segundos);

        puntos = Math.max(puntos, 0);

        if (!victoria) {
            puntos = 0;
        }

        return puntos;
    }

    private void actualizarTiempoPartida() throws PartidaException {
        if (partidaActual == null) {
            return;
        }
        int tiempoActual = partidaActual.getTiempoRestante();

        if (tiempoActual > 0) {
            partidaActual.setTiempoRestante(tiempoActual - 1);
        }
    }

    @Override
    public int obtenerTiempoPartida() {
        return partidaActual.getTiempoRestante();
    }

    @Override
    public boolean comprobarVictoria() {
        victoria = mazo.getCartas().stream().allMatch(carta -> carta.isEmparejada() == true);

        return victoria;
    }

    @Override
    public boolean comprobarDerrota() {
        return !victoria;
    }

    @Override
    public boolean getVoleo() {
        return primerVolteo;
    }

    @Override
    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    @Override
    public byte[] getCartaImage(CartaMemory carta) {
        try {
            return carta.isGirada() ? carta.getImage() : carta.getBackOfCardImage();
        } catch (IOException ex) {
            return null;
        }
    }

}
