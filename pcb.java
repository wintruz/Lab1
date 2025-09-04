import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

public class pcb {
    //Atributos
    String id_PCB; // ID del proceso. Ej, P1
    int cant_Instruc; // Cantidad de instrucciones a realizar en el CPU
    String estado; // Estado del proceso
    String bloqueo; // Almacena "Si" o "No" de manera aleatoria
    int pos_Cola; // Posicion de llegada del proceso en la cola de ejecucion
    int tiempo_Cola; // Tiempo del proceso esperando en la cola para ejecutarse
    int tiempo_Bloqueo; // Tiempo que pasa el proceso en el estado de bloqueo
    int tiempo_Ejecutado; // Tiempo que ha pasado el proceso ejecutandose en el CPU

    //Constructor
    public pcb(){ // Constructor vacio
        
    }


    public pcb(int i){
        this.id_PCB = "P" + i;
    }

    // Metodos
    public ArrayList<pcb> procesos() { //Creacion inicial de la lista de procesos de los n procesos dados por el usuario
        ArrayList<pcb> listaProcesos = new ArrayList<>();
        pcb temp = new pcb();

        // Se obtienen la cantidad de procesos con Random
        int cant;

        // Creación de los n procesos en la lista
        for(int i = 0;i < cant;i++){
            temp = new pcb(i+1);
            listaProcesos.add(temp);
        }

        return listaProcesos;
    }
}
