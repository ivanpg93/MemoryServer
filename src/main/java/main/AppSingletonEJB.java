/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author manel
 * 
 * 
 */
@Singleton //A Singleton Session Bean maintains the state of the bean for the complete lifecycle of the application.
@Startup //the javax.ejb.Startup annotation to inform the EJB container to initialize the bean at the startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
public class AppSingletonEJB {
    
  
    
    // versió i data del servidor
    private static final String APP_VERSION = "1.0.0";
    private static final String DATE_VERSION = "12/05/2023";
    
    private static final Logger log = Logger.getLogger(AppSingletonEJB.class.getName());
    
    // darrera actualització de la BBDD
    private Date lastUpdateDBUTC;
    
    // moment d'inici de l'aplicació
    private Date uptimeUTC;
    
    @PostConstruct //With this annotation, it'll be called by the container upon instantiation of the bean
    public void initialize() 
    {
        this.showLogo();
        
        this.lastUpdateDBUTC = new Date();
        
        this.uptimeUTC = new Date();
        
        log.log(Level.INFO, "Inicialitzant AppSingletonEJB.  lastUpdateDBUTC={0} , uptimeUTC={1}", new Object[]{this.lastUpdateDBUTC, this.uptimeUTC});
    }
    
    /***
     * Obté el timestamp d'inici de l'aplicació
     * @return 
     */
    @Lock(LockType.READ)
    public Date getUptimeUTC() {
        
        return this.uptimeUTC;
    }
    
    /***
     * Obtiene la última fecha de actualitzación de la BBDD
     * @return 
     */
    @Lock(LockType.READ)
    public Date getLastDBUpdateUTC() {
        
        return this.lastUpdateDBUTC;
    }

    /***
     * Establece la fecha de actualitzación de la BBDD
     */
    @Lock(LockType.WRITE)
    public void setLastDBUpdateUTC() {
        
        this.lastUpdateDBUTC = new Date();
        
    }
    
    /***
     * Mostra un banner amb la versión
     */
    private void  showLogo()
    { 
        
        String fileName = File.separator + "files" + File.separator + "banner.txt";
        StringBuilder banner = new StringBuilder();
        
        try {
            // arrel del context de fitxers del war
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            // carreguem el fixter ubicat a resoources
            InputStream input = classLoader.getResourceAsStream(fileName);
         
            // llegim el contingut
            for (byte b : input.readAllBytes())
            {
                banner.append((char)b);
            }
           
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error llegint fitxer de logo: {0} : {1}", new Object[]{fileName, ex.toString()});
        }
        
        banner.append(System.lineSeparator());
        banner.append(System.lineSeparator());
        banner.append("Versión del servidor: ").append(AppSingletonEJB.APP_VERSION).append(" de " + AppSingletonEJB.DATE_VERSION);
        banner.append(System.lineSeparator());
            
        log.log(Level.INFO, banner.toString());
    }
}
