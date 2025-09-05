import java.util.Random;

public class Main {
	
    public static void main(String[] args) {

        Random random = new Random();
        int cdc = 1;
        int quantum = genNumRandom(10,100);
        int num_procesos = genNumRandom(5, 10);
        boolean bloqueo;
        pcb proceso;

        for(int i = 0; i < num_procesos; i++){
            bloqueo = (genNumRandom(0, 1) == 1); // Asignacion de true en caso de que el valor dado sea 1, false en caso contrario
            // Habria que crear variables para cada atributo y despues pasarlo al constructor
            proceso = new pcb();
        }
    }
}

// Posible metodo para generar los numeros randoms solicitados para la simulacion
public static int genNumRandom(int rInf, int rSup){

    // Semilla para evitar repeticion de numeros aleatorios
    long semilla = System.currentTimeMillis();

    // Inicializacion de la variable que genera el numero aleatorio con la semilla
    Random random = new Random(semilla);

    return random.nextInt(rSup - rInf + 1) + rInf;
}
