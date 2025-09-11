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
        int cdc = 1; // cambio de contexto en ms (fijo)
        int quantum = random.nextInt(100 - 10 + 1) + 10; // 10-100 ms

        System.out.println("Ingrese la cantidad de procesos: ");
        int num_procesos = sc.nextInt();
        System.out.println("Quantum: " + quantum + "ms");
        System.out.println("CDC inicial: " + cdc + "\n");

        ArrayList<PCBP> listapcb = new ArrayList<>();              // cola de listos (se usará ordenada por SJF)
        ArrayList<PCBP> listaBloqueadospcb = new ArrayList<>();    // cola de bloqueados
        ArrayList<PCBP> listaTerminadospcb = new ArrayList<>();    // terminados

        // Generar procesos aleatorios y mostrar tabla inicial
        System.out.printf("%-10s %-10s %-15s %-10s %-10s %-10s %-12s\n",
                "ID-Proceso", "Pos.Cola", "Cant.Inst.", "Estado", "CDC", "Bloqueo", "Time Block");

        System.out.println("------------------------------------------------------------------------------------------------");

        for (int i = 0; i < num_procesos; i++) {
            boolean bloqueo = random.nextBoolean();
            int procesos = random.nextInt(100 - 5 + 1) + 5;
            int tiempoBloqueo = bloqueo ? random.nextInt(8 - 2 + 1) + 2 : 0;
            System.out.printf("%-10s %-10d %-15d %-10s %-10d %-10s %-12s\n",
                    "P" + (i + 1),
                    (i + 1),
                    procesos,
                    "N",
                    0,
                    bloqueo ? "Si" : "No",
                    tiempoBloqueo + "ms");

            listapcb.add(new PCBP(i + 1, procesos, bloqueo, "N", tiempoBloqueo));
        }
        System.out.println("------------------------------------------------------------------------------------------------\n\n");

        // Poner todos en Listos (L)
        for (PCBP p : listapcb) {
            p.setEstado("L");
        }

        // Estado inicial impreso
        salidaPCB(listapcb);
        sc.nextLine();

        // Variables de simulacion
        PCBP procesoEnCPU = null;
        int tiempoSliceRestante = 0; // cuantos ms le quedan al quantum en esta asignacion
        boolean primerTick = true;
        int tiempoSimulado = 0; // contador global de ms (opcional para debug)

        // LOOP principal: continúa hasta que no queden procesos ni en listos ni bloqueados ni en CPU
        while (!listapcb.isEmpty() || !listaBloqueadospcb.isEmpty() || procesoEnCPU != null) {
            // --- 1 ms tick: actualizar 'globalmente' todos los tiempos antes de cualquier asignacion nueva
            tiempoSimulado++;

            // 1) Si hay proceso en CPU, ejecuta 1 ms
            if (procesoEnCPU != null) {
                // Si aún no se marcó que está en ejecución, marcar estado y contar CDC (ya debió haberse contado al asignarlo)
                procesoEnCPU.setCantidadInstrucciones(Math.max(0, procesoEnCPU.getCantidadInstrucciones() - 1));
                procesoEnCPU.setTiempoEjecucion(procesoEnCPU.getTiempoEjecucion() + 1);
                procesoEnCPU.setTiempoTotal(procesoEnCPU.getTiempoTotal() + 1);
                tiempoSliceRestante--; // consumimos 1 ms del quantum
            }

            // 2) Todos los procesos en lista de listos incrementan su tiempo en cola y total
            for (PCBP p : listapcb) {
                if (p.getEstado().equals("L")) {
                    p.setTiempoCola(p.getTiempoCola() + 1);
                    p.setTiempoTotal(p.getTiempoTotal() + 1);
                }
            }

            // 3) Procesos bloqueados: disminuir su tiempo de bloqueo, aumentar tBloqTotal y tiempoTotal.
            //    Si llegan a 0, pasarlos de vuelta a listos (L).
            Iterator<PCBP> it = listaBloqueadospcb.iterator();
            while (it.hasNext()) {
                PCBP p = it.next();
                p.setTiempoBloqueo(p.getTiempoBloqueo() - 1);
                p.setTiempoTotBloq(p.getTiempoTotBloq() + 1);
                p.setTiempoTotal(p.getTiempoTotal() + 1);

                if (p.getTiempoBloqueo() <= 0) {
                    p.setEstado("L");
                    p.setCambioContexto(p.getCambioContexto() + cdc); // cuenta como cambio al reingresar
                    listapcb.add(p);
                    it.remove();
                }
            }

            /* DECISIONES: manejo de bloqueo por primera ejecucion
            Si hay proceso en CPU y tiene bloqueo y no ha sido bloqueado aún, la especificación
            dice: "El bloqueo será ejecutado después de estar 1 ms en el CPU la primera vez que se le asigne".
            Para implementar eso: cuando asignamos a CPU por primera vez marcamos yaFueBloqueado=false, y en el tick
            después de su primer ms lo movemos a bloqueados.*/

            if (procesoEnCPU != null && procesoEnCPU.getBloqueo() && !procesoEnCPU.isYaFueBloqueado()) {
                // Si ya consumió 1 ms de ejecución en esta asignación, se bloquea.
                // Detectar: si en el slice restante quedó == (quantum-1) al asignarlo? manejaremos con la bandera yaFueBloqueado:
                // Sabemos que cuando lo asignamos lo dejamos yaFueBloqueado == false. Tras ejecutarse 1 ms lo marcamos y lo movemos.
                // Aquí lo movemos inmediatamente (ya le restamos 1 instrucción arriba).
                procesoEnCPU.setEstado("B");
                procesoEnCPU.setYaFueBloqueado(true);
                // dejar el tiempo de bloqueo que trae el PCB (ya estaba asignado en constructor)
                listaBloqueadospcb.add(procesoEnCPU);
                // removemos de CPU
                procesoEnCPU = null;
                tiempoSliceRestante = 0;
                // Al moverse a bloqueados, NO lo ponemos en la lista de listos
            }

            // --- Si el proceso en CPU terminó (instrucciones == 0)
            if (procesoEnCPU != null && procesoEnCPU.getCantidadInstrucciones() == 0) {
                procesoEnCPU.setEstado("T");
                // tiempoTotal ya se fue incrementando cada ms
                listaTerminadospcb.add(procesoEnCPU);
                // quitarlo de circulación
                procesoEnCPU = null;
                tiempoSliceRestante = 0;
            }

            // --- Si el slice llegó a 0 y hay proceso en CPU (no terminó ni se bloqueo), lo devolvemos a listos
            if (procesoEnCPU != null && tiempoSliceRestante <= 0) {
                // cooperativo: cuando termina su quantum, pasa a listo (si aún tiene instrucciones)
                procesoEnCPU.setEstado("L");
                procesoEnCPU.setCambioContexto(procesoEnCPU.getCambioContexto() + cdc);
                listapcb.add(procesoEnCPU);
                procesoEnCPU = null;
            }

            // --- Si CPU está libre, asignar el siguiente proceso por SJF (si hay)
            if (procesoEnCPU == null && !listapcb.isEmpty()) {
                // ordenar por cantidadInstrucciones (SJF)
                listapcb.sort(Comparator.comparingInt(PCBP::getCantidadInstrucciones));
                // tomar el primero
                procesoEnCPU = listapcb.remove(0);
                procesoEnCPU.setEstado("E");
                // al asignarlo contamos el cambio de contexto
                procesoEnCPU.setCambioContexto(procesoEnCPU.getCambioContexto() + cdc);
                // al momento de asignar le damos un slice de quantum (pero lo consumimos ms a ms)
                tiempoSliceRestante = quantum;
                // NOTA: la política de bloqueo "después de 1 ms la primera vez" depende de yaFueBloqueado=false
                // Si yaFueBloqueado era true, entonces no volverá a bloquearse.
            }

            // --- Impresiones: si hay bloqueados, mostrar ambas tablas, sino solo listos.
            if (listaBloqueadospcb.isEmpty()) {
                salidaPCB(concatListsForDisplay(procesoEnCPU, listapcb));
            } else {
                salidaPCBbloq(concatListsForDisplay(procesoEnCPU, listapcb), listaBloqueadospcb);
            }
            sc.nextLine();
        } // end while

        // Salida final de tiempos totales para cada proceso
        System.out.printf("\n\n%-40s\n", "Tiempos Totales");
        System.out.printf("%-12s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

        System.out.println("------------------------------------------------------------------------------------------------");
        for (PCBP p : listaTerminadospcb) {
            System.out.printf("%-12s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getTiempoCola(),
                    p.getCambioContexto(),
                    p.getTiempoTotBloq(),
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }
        System.out.println("------------------------------------------------------------------------------------------------\n\n");
        sc.close();
    }

    // Helper: prepara una lista para imprimir incluyendo el proceso en CPU al inicio (si existe).
    private static ArrayList<PCBP> concatListsForDisplay(PCBP procesoEnCPU, ArrayList<PCBP> listapcb) {
        ArrayList<PCBP> copia = new ArrayList<>();
        if (procesoEnCPU != null) {
            copia.add(procesoEnCPU);
        }
        copia.addAll(listapcb);
        return copia;
    }

    // Metodos para salida de la tabla 
    public static void salidaPCB(ArrayList<PCBP> listaAct) {
        System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

        System.out.println("------------------------------------------------------------------------------------------------");
        for (PCBP p : listaAct) {
            System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getCantidadInstrucciones(),
                    p.getEstado(),
                    p.getTiempoCola(),
                    p.getCambioContexto(),
                    p.getTiempoTotBloq(),
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }
        System.out.println("------------------------------------------------------------------------------------------------\n\n");
    }

    public static void salidaPCBbloq(ArrayList<PCBP> listaAct, ArrayList<PCBP> listBloqAct) {
        System.out.printf("%-12s %-15s %-8s %-12s %-8s %-12s %-12s %-12s\n",
                "ID-Proceso", "Cant.Inst.", "Estado", "Time Cola", "CDC", "Bloqueo", "Time Exe", "Total Time");

        System.out.println("------------------------------------------------------------------------------------------------");
        // Procesos bloqueados
        for (PCBP p : listBloqAct) {
            System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getCantidadInstrucciones(),
                    p.getEstado(),
                    p.getTiempoCola(),
                    p.getCambioContexto(),
                    p.getTiempoTotBloq(),
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }

        // Demas procesos
        for (PCBP p : listaAct) {
            System.out.printf("%-12s %-15d %-8s %-12d %-8d %-12s %-12d %-12d\n",
                    "P" + p.getId(),
                    p.getCantidadInstrucciones(),
                    p.getEstado(),
                    p.getTiempoCola(),
                    p.getCambioContexto(),
                    p.getTiempoTotBloq(),
                    p.getTiempoEjecucion(),
                    p.getTiempoTotal());
        }
        System.out.println("------------------------------------------------------------------------------------------------\n\n");
    }
}
