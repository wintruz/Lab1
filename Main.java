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
		boolean primeraIteracion = true;
        PCBP procesoActual = null;
        System.out.println("Quantum: "+ quantum +"ms");
        System.out.println("CDC inicial: "+cdc+"\n");
        //System.out.println(num_procesos);
        ArrayList<PCBP> listapcb = new ArrayList<>();
		ArrayList<PCBP> listaBloqueadospcb = new ArrayList<>();
		ArrayList<PCBP> listaTerminadospcb = new ArrayList<>();
		
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
            
            listapcb.add(new PCBP(i+1,procesos,bloqueo,"L",tiempoBloqueo));
        }

        listapcb.sort(Comparator.comparingInt(p -> p.getCantidadInstrucciones()));

        System.out.println("\n");
		
		System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

        for (PCBP p : listapcb) {
            System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getCantidadInstrucciones(),
                    p.getEstado(),
                    p.getTiempoCola(),
                    p.getCambioContexto(),
                    "0",
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }

		int cont = 0;
        while(!listapcb.isEmpty()){
			
				if(!primeraIteracion){
					// Aquí incrementas el tiempo en cola de los procesos que están en "L"
					for (int i = 1; i < listapcb.size(); i++) {
						PCBP p = listapcb.get(i);
						if (p.getEstado().equals("L")) {
								p.setTiempoCola(p.getTiempoCola() + 1);
								p.setTiempoTotal(tiempoGlobal);
						}
					}
				} else {
						primeraIteracion = false; // Después de la primera iteración, ya no será true
				}
			tiempoGlobal++;
			procesoActual = listapcb.get(0);
			if(procesoActual.getEstado().equals("L")){
				procesoActual.setEstado("E");
				procesoActual.setCambioContexto(cdc);
				if(true){
					procesoActual.setTiempoTotal(cdc);
				}
			}
            procesoActual.setCantidadInstrucciones(procesoActual.getCantidadInstrucciones() - quantum);
			procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + quantum);
			procesoActual.setTiempoTotal(tiempoGlobal);
			
			//System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                //"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

            for (int i = 1; i < listapcb.size(); i++) {
				PCBP p = listapcb.get(i);
					if (p.getEstado().equals("L")) {
						p.setTiempoCola(p.getTiempoCola() + 1);
						p.setTiempoTotal(tiempoGlobal);
					}
			}
			// Si el proceso terminó
			if (procesoActual.getCantidadInstrucciones() <= 0) {
				procesoActual.setEstado("T");
				procesoActual.setCantidadInstrucciones(0);
				listapcb.remove(procesoActual);
			}
			
			System.out.println("------------------------------------\n");
			for (PCBP p : listapcb) {
			System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
					"P" + p.getId(),
					p.getCantidadInstrucciones(),
					p.getEstado(),
					p.getTiempoCola(),
					p.getCambioContexto(),
					"0",
					p.getTiempoEjecucion(),
					p.getTiempoTotal());
			}
			System.out.println("------------------------------------\n");
			// Ordenar la lista otra vez por SJF
			listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));
		}
    }
}


/*
	 for (PCBP p : listapcb) {
					
					p.setTiempoCola(tiempoGlobal);
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
*/