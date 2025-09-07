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
		int cInsProceso;
		boolean primeraIteracion = true;
        PCBP procesoActual = null;
        System.out.println("Quantum: "+ quantum +"ms");
        System.out.println("CDC inicial: "+cdc+"\n");
        //System.out.println(num_procesos);
        ArrayList<PCBP> listapcb = new ArrayList<>();
		ArrayList<PCBP> listaBloqueadospcb = new ArrayList<>();
		ArrayList<PCBP> listaTerminadospcb = new ArrayList<>();
		
		// Primera tabla
		// Procesos, Pos. Cola, Estado = N, CDC = 0, Bloqueo, Tiempo de bloqueo
        System.out.printf("%-10s %-10s %-15s %-10s %-10s %-10s %-12s\n",
                "ID-Proceso", "Pos.Cola", "Cant.Inst.", "Estado", "CDC", "Bloqueo", "Time Block");
		
		System.out.println("------------------------------------------------------------------------------------------------");
        for(int i = 0; i < num_procesos; i++){
            bloqueo = random.nextBoolean();
            procesos = random.nextInt(100 - 5 + 1) + 5;
            tiempoBloqueo = bloqueo == true ? random.nextInt(8-2+1)+2: 0;
			 System.out.printf("%-10s %-10d %-15d %-10s %-10d %-10s %-12s\n",
                    "P" +(i+1),
                    (i+1),
                    procesos,
                    "N",
                    0, // mismo valor inicial, es 0 al inicio de la tabla
                    bloqueo ? "Si" : "No",
                    tiempoBloqueo+"ms");
            
            listapcb.add(new PCBP(i+1,procesos,bloqueo,"N",tiempoBloqueo));
        }
		System.out.println("------------------------------------------------------------------------------------------------\n\n");

		
		// Impresion inicial de los procesos
		// Fase 0: Alistar procesos dentro del ciclo de vida
		for(PCBP p: listapcb){
			p.setEstado("L");
		}

		// Inicio de impresion de tabla para mostrar procesos listos
		System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");
		System.out.println("------------------------------------------------------------------------------------------------");

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
		System.out.println("------------------------------------------------------------------------------------------------\n\n");
		// Fin de impresion de tabla para mostrar procesos listos

		// int cont = 0; // Por ahora no se utiliza

		// Inicio del ciclo de vida de los procesos
        while(!listapcb.isEmpty()){
			
			// Caso principal a evaluar de manera iterativa, siendo la iteracion > 1
			if(!primeraIteracion){ // Evita cambiar de rama del if despues de la primera iteracion
				// Fase 2: Ejecucion de proceso mas corto
				// Utilizacion de objeto temporal para manejar el proceso mas corto
				procesoActual = listapcb.get(0);

				// Fase 2.5: Cambio de contexto
				if(procesoActual.getEstado().equals("L")){
					//Actualizar valores del proceso actual
					procesoActual.setEstado("E");
					procesoActual.setCambioContexto(cdc);
					procesoActual.setTiempoTotal(procesoActual.getTiempoTotal() + 1);

					// Actualizar valores de tiempo de cola y total para los demas procesos
					for (PCBP p: listapcb) {
						if(p.getEstado().equals("L")){
							p.setTiempoCola(p.getTiempoCola() + 1);
							p.setTiempoTotal(p.getTiempoTotal() + 1);
						}
					}

					// Inicio de salida de tabla cambio de contexto
					System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
						"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");
					
					System.out.println("------------------------------------------------------------------------------------------------");
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
					System.out.println("------------------------------------------------------------------------------------------------\n\n");
					// Fin de salida de tabla cambio de contexto (Proceso en ejecucion)
				}

				// Actualizar valores del proceso actual

				// Al utilizarse mucho el metodo getCantidadInstrucciones en esta parte se opta por una variable temporal
				cInsProceso = procesoActual.getCantidadInstrucciones();

				// Tiempo de ejecucion
				if((cInsProceso - quantum) > 0) {
					procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + quantum);
				} else {
					procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + cInsProceso);
				}

				// Tiempo total
				if(cInsProceso <= quantum) {
					procesoActual.setTiempoTotal(procesoActual.getTiempoTotal() + cInsProceso);
					// Actualizar valores para los demas procesos
					for (PCBP p: listapcb) {
							if (p.getEstado().equals("L")) {
								p.setTiempoCola(p.getTiempoCola() + cInsProceso);
								p.setTiempoTotal(p.getTiempoTotal() + cInsProceso);
							}
					}
				} else {
					procesoActual.setTiempoTotal(procesoActual.getTiempoTotal() + quantum);
					// Actualizar valores para los demas procesos
					for (PCBP p: listapcb) {
							if (p.getEstado().equals("L")) {
								p.setTiempoCola(p.getTiempoCola() + quantum);
								p.setTiempoTotal(p.getTiempoTotal() + quantum);
							}
					}
				}

				// Cantidad de instrucciones
				if((cInsProceso - quantum) >= 0){ // Evita numeros negativos
					procesoActual.setCantidadInstrucciones(cInsProceso - quantum);
				} else {
					procesoActual.setCantidadInstrucciones(0);
				}

				// Inicio de salida de tabla (Proceso en ejecucion)
				System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
					"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");
				
				System.out.println("------------------------------------------------------------------------------------------------");
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
				System.out.println("------------------------------------------------------------------------------------------------\n\n");
				// Fin de salida de tabla (Proceso en ejecucion)

				// Si el proceso terminó
				if (procesoActual.getCantidadInstrucciones() == 0) {
					procesoActual.setEstado("T");

					// Inicio de salida de tabla (Proceso terminado)
					System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
						"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");
					
					System.out.println("------------------------------------------------------------------------------------------------");
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
					System.out.println("------------------------------------------------------------------------------------------------\n\n");
					// Fin de salida de tabla (Proceso terminado)

					listapcb.remove(procesoActual); // Removiendo proceso terminado de la lista
				}

				// Ordenar la lista otra vez por SJF
				listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));

			// Caso secundario
			} else { // Primera iteracion

				// Fase 1: Prioridad de procesos
				// Ordenacion de menor a mayor cantidad de instrucciones
				listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));

				// Incrementacion del tiempo en cola de los procesos que están en "L"
				for (PCBP p: listapcb) { // Para evitar llamar en cada iteracion al metodo .size() y la reinicializacion de p
					if (p.getEstado().equals("L")) {
							p.setTiempoCola(p.getTiempoCola() + 1);
							p.setTiempoTotal(1);
					}
				}

				// Inicio impresion de organizacion de procesos
				System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
						"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

				System.out.println("------------------------------------------------------------------------------------------------");
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
				System.out.println("------------------------------------------------------------------------------------------------\n\n");
				// Fin impresion de organizacion de procesos

				primeraIteracion = false; // Después de la primera iteración, ya no se entrara en este bloque
			}
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