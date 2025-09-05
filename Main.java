import java.util.Random;
import java.util.ArrayList;

public class Main {
	
    public static void main(String[] args) {

        Random random = new Random();
        int cdc = 1, procesos;
        int quantum = random.nextInt(100 - 10 + 1) + 10;
        int num_procesos = random.nextInt(10) + 1;
        boolean bloqueo;
        System.out.println(quantum);
        System.out.println(num_procesos);
        ArrayList<PCBP> listapcb = new ArrayList<>();

        for(int i = 0; i < num_procesos; i++){
            bloqueo = random.nextBoolean();
            System.out.println(bloqueo);
            procesos = random.nextInt(100 - 5 + 1) + 5;
            System.out.println(procesos);
            listapcb.add(new PCBP(i+1,procesos,bloqueo,"listo"));
        }
    }
}
