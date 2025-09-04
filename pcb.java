import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

public class pcb {
    //Atributos
    int quantum; //Cantidad de instrucciones que puede realizar la CPU en un ms. Aleatorio entre 10-100 ms
    String id_PCB; // ID del proceso. Ej, P1
    int cant_Instruc; // Cantidad de instrucciones a realizar en el CPU
    String estado; // Estado del proceso
    int cdc; // Cambio de contexto. Fijo en 1 ms
    String bloqueo; // Almacena "Si" o "No" de manera aleatoria
    int pos_Cola; // Posicion de llegada del proceso en la cola de ejecucion
    int tiempo_Cola; // Tiempo del proceso esperando en la cola para ejecutarse
    int tiempo_Bloqueo; // Tiempo que pasa el proceso en el estado de bloqueo
    int tiempo_Ejecutado; // Tiempo que ha pasado el proceso ejecutandose en el CPU

    //Constructor
    
}
