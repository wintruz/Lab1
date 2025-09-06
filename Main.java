import java.util.Random;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;
public class Main {
	
    public static void main(String[] args) {

        Random random = new Random();
        int cdc = 1, procesos,tiempoBloqueo;
        //Queue<PCBP> listaPcbs = new PriorityQueue<>();
        int quantum = random.nextInt(100 - 10 + 1) + 10;
        int num_procesos = random.nextInt(10) + 1;
        boolean bloqueo;
        int tiempoGlobal = 0;
        PCBP procesoActual = null;
        System.out.println("Quantum: "+ quantum +"ms");
        System.out.println("CDC inicial: "+cdc+"\n");
        //System.out.println(num_procesos);
        ArrayList<PCBP> listapcb = new ArrayList<>();

        System.out.printf("%-10s %-10s %-15s %-10s %-10s %-10s %-12s\n",
                "ID-Proceso", "Pos.Cola", "Cant.Inst.", "Estado", "CDC", "Bloqueo", "Time Block");
        for(int i = 0; i < num_procesos; i++){
            bloqueo = random.nextBoolean();
            procesos = random.nextInt(100 - 5 + 1) + 5;
            tiempoBloqueo = bloqueo == true ? random.nextInt(8-2+1)+2: 0;
			 System.out.printf("%-10s %-10d %-15d %-10s %-10d %-10s %-12s\n",
                    "P" +(i+1),
                    (i+1),
                    procesos,
                    "N",
                    cdc, // mismo valor inicial
                    bloqueo ? "Si" : "No",
                    tiempoBloqueo+"ms");
            
            listapcb.add(new PCBP(i+1,procesos,bloqueo,"listo",tiempoBloqueo));
        }

        listapcb.sort(Comparator.comparingInt(p -> p.getCantidadInstrucciones()));

        System.out.println("\n\n");
		
		System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

        for (PCBP p : listapcb) {
            System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getCantidadInstrucciones(),
                    p.getEstado(),
                    p.getTiempoCola(),
                    cdc,
                    "0",
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }


        while(!listapcb.isEmpty()){
			
		}
    }
}
