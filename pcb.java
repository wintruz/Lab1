import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

public class pcb {
    //Atributos
    String id_PCB; // ID del proceso. Ej, P1
    int cant_Instruc; // Cantidad de instrucciones a realizar en el CPU
    char estado; // Estado del proceso
    boolean bloqueo; // Almacena "Si" o "No" de manera aleatoria
    int tiempo_Cola; // Tiempo del proceso esperando en la cola para ejecutarse
    int tiempo_CDC;
    int tiempo_Bloqueo; // Tiempo que pasa el proceso en el estado de bloqueo
    int tiempo_Ejecutado; // Tiempo que ha pasado el proceso ejecutandose en el CPU

    //Constructor
    public pcb(){ // Constructor vacio
        
    }


    public pcb(String id, int cInstr, boolean bloq, int tCola, int tCDC, int tBloq, int tEjec){
        this.id_PCB = id;
        this.cant_Instruc = cInstr;
        this.estado = 'N';
        this.bloqueo = bloq;
        this.tiempo_Cola = tCola;
        this.tiempo_CDC = tCDC;
        this.tiempo_Bloqueo = tBloq;
        this.tiempo_Ejecutado = tEjec;
    }

    // Metodos

    // Metodos para impresion en pantalla

    //Impresion de tabla al entrar en el ciclo de ejecucion de los procesos
    public void tabla_PCB_itr(ArrayList<pcb> procesos){
        //Impresion de encabezado
        System.out.printf("%-10s %-12s %-10s %-10s %-10s %-10s %-10s %-10s %s%n",
                "ID-Proceso", "Cant. Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time", "");

        pcb temp = new pcb();

        for(int i = 0, n = procesos.size();i < n;i++){
            temp = procesos.get(i); // Obtencion del i elemento de la lista

            // Inicio impresion fila del proceso i
            System.out.printf("%-10s %-12d %-10s %-10d %-10d %-10d %-10d %-10d %s%n",
            temp.get_idPCB() + temp.get_cInstruc() + temp.get_estado() + temp.get_tCDC() + 
            temp.get_tBloqueo() + temp.get_tEjecutado());
            // Fin de impresion fila de proceso i
        }
    }

    // Metodos get
    public String get_idPCB() { // Retorna el valor de idPCB
        return this.id_PCB;
    }

    public int get_cInstruc(){ // Retorna el valor de la cantidad de instrucciones
        return this.cant_Instruc;
    }

    public char get_estado(){ // Retorna el caracter del estado del proceso
        return this.estado;
    }

    public int get_tCDC(){ // Retorna el caracter del estado del proceso
        return this.tiempo_CDC;
    }

    public boolean get_bloqueo(){ // Retorna el valor booleano del proceso de bloqueo
        return this.bloqueo;
    }

    public int get_tCola(){ // Retorna el valor del tiempo de cola o espera actual del procesador
        return tiempo_Cola;
    }

    public int get_tBloqueo(){ // Retorna el valor del tiempo de bloqueo actual del proceso
        return this.tiempo_Bloqueo;
    }

    public int get_tEjecutado(){ // Retorna el tiempo de ejecucion actual del proceso
        return this.tiempo_Ejecutado;
    }
}
