package cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi.ConexionCliente;
import rmi_interface.Interface;

/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */
public class Cliente implements Runnable {
    public static int Puerto = 2014;                                 //Número del puerto que está alojado el servidor
    public static String IPServer = "localhost";                    //Dirección IP del servidor, la cual podría utilizarse por defecto el localhost
    public static String nombreReferenciaRemota = "Ejemplo-Síncrono-RMI"; // Nombre del objeto subido

    public static Thread thread;    //Hebra que utilizaremos para poder realizar el CallBack

    public static void main(String[] args) {

        //Inicializamos la hebra de esta clase
        thread = new Thread(new Cliente());
        //Y hacemos partir a ésta
        thread.start();

        Interface objetoRemoto; //Se crea un nuevo objeto llamado objetoRemoto

        //Se instancia el objeto que conecta con el servidor
        ConexionCliente conexion = new ConexionCliente();
        try {
            //Se conecta con el servidor
            if (conexion.iniciarRegistro(IPServer, Puerto, nombreReferenciaRemota)) {

                //Se obtiene la referencia al objeto remoto
                objetoRemoto = conexion.getServidor();

                int opcion = 0;
                while (opcion != 4) {

                    //Escoger alguna opción del menú
                    System.out.println("Menú RMI\nElija una opción para mostrarla a todos\n1. Buenos días\n2. Buenas tardes\n3. Buenas noches\n4. Salir");
                    Scanner sc = new Scanner(System.in);
                    opcion = Integer.parseInt(sc.next());

                    if (opcion == 1 || opcion == 2 || opcion == 3) {
                        //Llama a un método del objeto remoto, y se le ingresa un parámetro a éste método
                        objetoRemoto.enviarMensaje(opcion);
                    } else if (opcion != 4) {
                        System.out.println("Ingrese un número válido por favor...");
                    }
                }
            }

        } catch (RemoteException | NumberFormatException e) {
            System.out.println("Ha ocurrido un error..." + e);
        }

        System.exit(0);

    }

    @Override
    public void run() {
        String mensaje = new String(); //String del mensaje entregado por el servidor
        Interface objetoRemotoHebra; //Se crea un nuevo objeto llamado objetoRemoto

        //Se instancia el objeto que conecta con el servidor
        ConexionCliente conexion = new ConexionCliente();
        try {
            //Se conecta con el servidor
            if (conexion.iniciarRegistro(IPServer, Puerto, nombreReferenciaRemota)) {

                //Se obtiene la referencia al objeto remoto
                objetoRemotoHebra = conexion.getServidor();

                while (true) {
                    //Obtenemos el valor entregado por el servidor
                    String mensajeActual = objetoRemotoHebra.mensajeBroadcast();
                    //De ser distinto al mensaje anterior, cambiamos el valor
                    //del mensaje nuevo y lo imprimimos
                    if(!mensajeActual.equals(mensaje)){
                        mensaje = mensajeActual;                                
                        System.out.println(mensaje);
                    }
                    //La hebra se deja dormida 1 segundo, por lo tanto,
                    //cada 1 segundo estará consulta al servidor si existe
                    //un nuevo mensaje que entregar
                    Thread.sleep(1000);
                }
            }
        } catch (RemoteException | InterruptedException e) {
            System.out.println("Ha ocurrido un error..." + e);
        }
    }
}
