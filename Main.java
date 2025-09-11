import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Iterator;

public class Main {
	public static void main(String[] args) {

		Random random = new Random();
		Scanner sc = new Scanner(System.in);
		int cdc = 1, procesos, tiempoBloqueo;
		int quantum = random.nextInt(100 - 10 + 1) + 10;

		int num_procesos = 0;
		boolean bloqueo;
		int cInsProceso;
		boolean primeraIteracion = true;
		PCBP procesoActual = null;

		System.out.println("Ingrese la cantidad de procesos: ");
		num_procesos = sc.nextInt();
		System.out.println("Quantum: " + quantum + "ms");
		System.out.println("CDC inicial: " + cdc + "\n");

		ArrayList<PCBP> listapcb = new ArrayList<>();
		ArrayList<PCBP> listaBloqueadospcb = new ArrayList<>();
		ArrayList<PCBP> listaTerminadospcb = new ArrayList<>();

		// Primera tabla
		// Creacion de procesos
		System.out.printf("%-10s %-10s %-15s %-10s %-10s %-10s %-12s\n",
				"ID-Proceso", "Pos.Cola", "Cant.Inst.", "Estado", "CDC", "Bloqueo", "Time Block");

		System.out.println(
				"------------------------------------------------------------------------------------------------");
		for (int i = 0; i < num_procesos; i++) {
			bloqueo = random.nextBoolean();
			procesos = random.nextInt(100 - 5 + 1) + 5;
			tiempoBloqueo = bloqueo == true ? random.nextInt(8 - 2 + 1) + 2 : 0;
			System.out.printf("%-10s %-10d %-15d %-10s %-10d %-10s %-12s\n",
					"P" + (i + 1),
					(i + 1),
					procesos,
					"N",
					0, // mismo valor inicial, es 0 al inicio de la tabla
					bloqueo ? "Si" : "No",
					tiempoBloqueo + "ms");

			listapcb.add(new PCBP(i + 1, procesos, bloqueo, "N", tiempoBloqueo));
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------\n\n");

		// Impresion inicial de los procesos
		// Fase 0: Alistar procesos dentro del ciclo de vida
		for (PCBP p : listapcb) {
			p.setEstado("L");
		}

		// Impresion de tabla para mostrar procesos listos
		salidaPCB(listapcb, listaBloqueadospcb);
		sc.nextLine();

		// Inicio del ciclo de vida de los procesos
		while (!listapcb.isEmpty()) {
			// huboCambios = false;

			// Caso principal a evaluar de manera iterativa, siendo la iteracion > 1
			if (!primeraIteracion) { // Evita cambiar de rama del if despues de la primera iteracion
				// Fase 2: Ejecucion de proceso mas corto
				// Utilizacion de objeto temporal para manejar el proceso mas corto
				procesoActual = listapcb.get(0);
				do {
					// Nuevo proceso actual
					if(!listapcb.isEmpty()){
						procesoActual = listapcb.get(0);
					}

					// Fase 2.5: Cambio de contexto
					if (procesoActual.getEstado().equals("L")) {
						// Actualizar valores del proceso actual
						procesoActual.setEstado("E");
						procesoActual.setCambioContexto(procesoActual.getCambioContexto() + cdc);
						if(procesoActual.getBloqueo()){
							procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + 1);
						}

						// Actualizar valores de tiempo de cola y total para los demas procesos
						for (PCBP p : listapcb) {
							if (p.getEstado().equals("L")) {
								p.setTiempoCola(p.getTiempoCola() + 1);
							}
						}

						Iterator<PCBP> it = listaBloqueadospcb.iterator();
						// Procesos bloqueados
						while (it.hasNext()) {
							PCBP p = it.next();
							p.setTiempoBloqueo(p.getTiempoBloqueo() - 1);
							p.setTiempoTotBloq(p.getTiempoTotBloq() + 1);

							if (p.getTiempoBloqueo() <= 0) {
								p.setEstado("L");
								p.setCambioContexto(p.getCambioContexto() + cdc);
								listapcb.add(p);

								// Reordenacion de procesos
								listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));

								// Reconsiderar el nuevo proceso mas corto
								if(p.getCantidadInstrucciones() >= procesoActual.getCantidadInstrucciones()){ // El proceso bloqueado tiene menos instrucciones
									// Se realiza el cambio de contexto en el proceso actual
									procesoActual.setEstado("L");
									// procesoActual.setCambioContexto(procesoActual.getCambioContexto() + cdc);
								}
								it.remove(); // Remover proceso al terminar su bloqueo de la lista de proceso bloqueados
							}
						}
						// huboCambios = true;
						salidaPCB(listapcb, listaBloqueadospcb);
						sc.nextLine();
					}

					// Fase 3: Verificar opcion de bloqueo
					if (procesoActual.getBloqueo() || listaBloqueadospcb.size() > 0) {

						// Si el proceso no ha sido bloqueado
						if(!procesoActual.isYaFueBloqueado() && procesoActual.getBloqueo()){
							// Ejecuta solo 1ms
							procesoActual.setCantidadInstrucciones(procesoActual.getCantidadInstrucciones() - 1);
							procesoActual.setCambioContexto(procesoActual.getCambioContexto() + cdc);

							Iterator<PCBP> it = listaBloqueadospcb.iterator();
							// Procesos bloqueados
							while (it.hasNext()) {
								PCBP p = it.next();
								p.setTiempoBloqueo(p.getTiempoBloqueo() - 1);
								p.setTiempoTotBloq(p.getTiempoTotBloq() + 1);

								if (p.getTiempoBloqueo() <= 0) {
									p.setEstado("L");
									p.setCambioContexto(p.getCambioContexto() + cdc);
									listapcb.add(p);
									it.remove(); // Usar el método remove() del iterador // Agregar salida por si se remueve
								}
							}

							// Cambiar a estado bloqueado
							procesoActual.setEstado("B");
							procesoActual.setYaFueBloqueado(true); // para que no vuelva a entrar aquí
							listaBloqueadospcb.add(procesoActual);
							listapcb.remove(procesoActual);

							// Demas procesos
							for (PCBP p1 : listapcb) {
								if(p1.getEstado().equals("L") && !p1.isYaFueBloqueado()){ // Solo para los procesos Listos
									p1.setTiempoCola(p1.getTiempoCola() + 1);
								}else if(p1.getEstado().equals("E")){ // Solo para el proceso en ejecucion
									p1.setTiempoEjecucion(p1.getTiempoEjecucion() + 1);
									p1.setCantidadInstrucciones(p1.getCantidadInstrucciones() - 1);
								}
							}
							// huboCambios = true;
							salidaPCB(listapcb, listaBloqueadospcb);
							sc.nextLine();
						}else {
							// Actualizar valores

							Iterator<PCBP> it = listaBloqueadospcb.iterator();
							// Procesos bloqueados
							while (it.hasNext()) {
								PCBP p = it.next();
								p.setTiempoBloqueo(p.getTiempoBloqueo() - 1);
								p.setTiempoTotBloq(p.getTiempoTotBloq() + 1);

								if (p.getTiempoBloqueo() <= 0) {
									p.setEstado("L");
									p.setCambioContexto(p.getCambioContexto() + cdc);
									listapcb.add(p);
									it.remove(); // Usar el método remove() del iterador // Agregar salida por si se remueve
								}
							}

							// Demas procesos
							for (PCBP p1 : listapcb) {
								if(p1.getEstado().equals("L") && !p1.isYaFueBloqueado()){ // Solo para los procesos Listos
									p1.setTiempoCola(p1.getTiempoCola() + 1);
								}else if(p1.getEstado().equals("E")){ // Solo para el proceso en ejecucion
									p1.setTiempoEjecucion(p1.getTiempoEjecucion() + 1);
									p1.setCantidadInstrucciones(p1.getCantidadInstrucciones() - 1);
								}
							}
							// huboCambios = true;
							salidaPCB(listapcb, listaBloqueadospcb);
							sc.nextLine();
						}
					}
					// Reconsideracion del proceso mas corto
					for(PCBP p : listapcb){
						if(p.getCantidadInstrucciones() < procesoActual.getCantidadInstrucciones()){
							procesoActual.setEstado("L");
							procesoActual.setCambioContexto(procesoActual.getCambioContexto() + 1);

							// Demas procesos
							for (int i = 1, n = listapcb.size(); i < n; i++) {
								PCBP p1 = listapcb.get(i);
								if(p1.getEstado().equals("L") && !p1.isYaFueBloqueado()){ // Solo para los procesos Listos
									p1.setTiempoCola(p1.getTiempoCola() + 1);
								}
							}

							// Ordenar la lista otra vez por SJF
							listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));

							// Nuevo Proceso con menos instrucciones
							procesoActual = listapcb.get(0);
							procesoActual.setEstado("E"); // Cambio a ejecucion
							salidaPCB(listapcb, listaBloqueadospcb);
							sc.nextLine();
							break;
						}
					}
					
					listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));
				}while(!listaBloqueadospcb.isEmpty()); // Mientras aun haya procesos bloqueados

				// Actualizar valores del proceso actual

				// Al utilizarse mucho el metodo getCantidadInstrucciones en esta parte se opta
				// por una variable temporal
				cInsProceso = procesoActual.getCantidadInstrucciones();

				// Tiempo de ejecucion
				if ((cInsProceso - quantum) > 0) {
					procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + quantum);
				} else {
					procesoActual.setTiempoEjecucion(procesoActual.getTiempoEjecucion() + cInsProceso);
				}

				// Tiempo de Cola
				if (cInsProceso <= quantum) {
					for (PCBP p : listapcb) {
						if (p.getEstado().equals("L")) {
							p.setTiempoCola(p.getTiempoCola() + cInsProceso);
						}
					}
				} else {
					for (PCBP p : listapcb) {
						if (p.getEstado().equals("L")) {
							p.setTiempoCola(p.getTiempoCola() + quantum);
						}
					}
				}

				// Cantidad de instrucciones
				if ((cInsProceso - quantum) >= 0) { // Evita numeros negativos
					procesoActual.setCantidadInstrucciones(cInsProceso - quantum);
				} else {
					procesoActual.setCantidadInstrucciones(0);
				}

				// Salida de tabla (Proceso en ejecucion)
				salidaPCB(listapcb, listaBloqueadospcb);
				sc.nextLine();

				// Si el proceso terminó
				if (procesoActual.getCantidadInstrucciones() == 0) {
					procesoActual.setEstado("T");

					// Inicio de salida de tabla (Proceso terminado)
					salidaPCB(listapcb, listaBloqueadospcb);
					sc.nextLine();

					listaTerminadospcb.add(procesoActual);
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
				for (PCBP p : listapcb) { // Para evitar llamar en cada iteracion al metodo .size() y la
											// reinicializacion de p
					if (p.getEstado().equals("L")) {
						p.setTiempoCola(p.getTiempoCola() + 1);
					}
				}

				// Inicio impresion de organizacion de procesos
				salidaPCB(listapcb, listaBloqueadospcb);
				sc.nextLine();

				primeraIteracion = false; // Después de la primera iteración, ya no se entrara en este bloque
			}
		}
		// Salida final de tiempos totales para cada proceso
		System.out.printf("\n\n%-40s\n", "Tiempos Totales");
		System.out.printf("%-12s %-12s %-8s %-12s %-12s %-12s\n",
				"ID-Proceso", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

		System.out.println(
				"------------------------------------------------------------------------------------------------");
		for (PCBP p : listaTerminadospcb) {
			System.out.printf("%-12s %-12d %-8d %-12s %-12d %-12d\n",
					"P" + p.getId(),
					p.getTiempoCola(),
					p.getCambioContexto(),
					p.getTiempoTotBloq(),
					p.getTiempoEjecucion(),
					p.getTiempoTotal());
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------\n\n");
	}

	// Metodo para salida de la tabla 
	public static void salidaPCB(ArrayList<PCBP> listaAct, ArrayList<PCBP> listBloqAct) {
		int tiempoTotal = 0;
		System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
				"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

		System.out.println(
				"------------------------------------------------------------------------------------------------");
		// Procesos bloqueados
		for (PCBP p : listBloqAct) {
			tiempoTotal = p.getTiempoCola() + p.getTiempoEjecucion() + p.getTiempoTotBloq() + p.getCambioContexto();
			p.setTiempoTotal(tiempoTotal);
			System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
					"P" + p.getId(),
					p.getCantidadInstrucciones(),
					p.getEstado(),
					p.getTiempoCola(),
					p.getCambioContexto(),
					p.getTiempoTotBloq(),
					p.getTiempoEjecucion(),
					tiempoTotal);
		}

		// Demas procesos
		for (PCBP p : listaAct) {
			tiempoTotal = p.getTiempoCola() + p.getTiempoEjecucion() + p.getTiempoTotBloq() + p.getCambioContexto();
			p.setTiempoTotal(tiempoTotal);
			System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
					"P" + p.getId(),
					p.getCantidadInstrucciones(),
					p.getEstado(),
					p.getTiempoCola(),
					p.getCambioContexto(),
					p.getTiempoTotBloq(),
					p.getTiempoEjecucion(),
					tiempoTotal);
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------\n\n");
	}

	// Función para escribir la tabla de procesos en un archivo de texto
	public static void escribirTablaEnArchivo(ArrayList<PCBP> listaAct, String nombreArchivo) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo, false))) {

			bw.write(String.format("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
					"ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time"));
			bw.write(
					"------------------------------------------------------------------------------------------------\n");

			for (PCBP p : listaAct) {
				bw.write(String.format("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
						"P" + p.getId(),
						p.getCantidadInstrucciones(),
						p.getEstado(),
						p.getTiempoCola(),
						p.getCambioContexto(),
						p.getTiempoBloqueo(), // aquí podrías cambiar si implementas Bloqueo real
						p.getTiempoEjecucion(),
						p.getTiempoTotal()));
			}
			bw.write(
					"------------------------------------------------------------------------------------------------\n\n");

		} catch (IOException e) {
			System.out.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}
}
