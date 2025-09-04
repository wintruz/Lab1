import java.util.Random;

public class Main {
	
    public static void main(String[] args) {

        Random random = new Random();
        int cdc = 1;
        int quantum = random.nextInt(100 - 10 + 1) + 10;
        int num_procesos = random.nextInt(10) + 1;
        boolean bloqueo;

        for(int i = 0; i < num_procesos; i++){
            bloqueo = random.nextBoolean();
            PCB pcb = new PCB();
        }
    }
}
