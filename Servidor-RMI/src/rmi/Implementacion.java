package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi_interface.Interface;

/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */

public class Implementacion extends UnicastRemoteObject implements Interface {
    
    static String mensaje;
    
    static Logger logger;

    public Implementacion() throws RemoteException {
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Se ha instanciado la clase de Implementacion del Servidor");
        mensaje = new String("");
    }

    /*
     * Debo escribir todos los métodos que se encuentran en la interface
     */
    // Por cada metodo se escribe Override que se utiliza para que utilize este metodo en vez del metodo del padre
    @Override
    public void enviarMensaje(int opcion) throws RemoteException {
        logger.log(Level.INFO, "Se desea enviar mensaje a todos los usuarios");
        if(opcion == 1){
            mensaje = "Buenos días";
        }
        else if(opcion == 2){
            mensaje = "Buenas tardes";
        }
        else{
            mensaje = "Buenas noches";
        }
        logger.log(Level.INFO, mensaje);
    }

    @Override
    public String mensajeBroadcast() throws RemoteException {
        logger.log(Level.INFO, "Se va a enviar el mensaje a todos los usuarios del servidor");
        return this.mensaje;
    }

}
