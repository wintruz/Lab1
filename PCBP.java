public class PCBP {
    
    // Atributos
    protected int idProceso;
    protected int cantidadInstrucciones;
    protected boolean bloqueo;
    protected String estado; // L, E, B, T
    protected int cambioContexto = 0;
    protected int tiempoBloqueo = 0;    // ciclos de bloqueo
    protected int tiempoCola = 0;       // tiempo en cola de listos
    protected int tiempoEjecucion = 0;  // tiempo ejecutado
    protected int tiempoTotal = 0;      // tiempo total transcurrido
    
    // Constructor
    public PCBP(int idProceso, int cantidadInstrucciones, boolean bloqueo, String estado, int tiempoBloqueo){
        this.idProceso = idProceso;
        this.cantidadInstrucciones = cantidadInstrucciones;
        this.bloqueo = bloqueo;
        this.tiempoBloqueo = bloqueo ? tiempoBloqueo : 0;
        this.estado = estado;
    }
    
    // Métodos
    public void cambiarEstado(String estado){
        this.estado = estado;
    }

    @Override
    public String toString(){
        return "ID: " + this.idProceso + 
               " | Instrucciones: " + this.cantidadInstrucciones + 
               " | Bloqueo: " + (this.bloqueo ? "Sí" : "No") + 
               " | Estado: " + this.estado;
    }

    // Getters
    public int getId() { return idProceso; }
    public int getCantidadInstrucciones() { return cantidadInstrucciones; }
    public String getEstado() { return estado; }
    public int getTiempoCola() { return tiempoCola; }
    public int getTiempoBloqueo() { return tiempoBloqueo; }
    public int getTiempoEjecucion() { return tiempoEjecucion; }
    public int getTiempoTotal() { return tiempoTotal; }
    public int getCambioContexto(){ return cambioContexto;}

    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    public void setCambioContexto(int cantidad){
        this.cambioContexto += cantidad;
    }
	public void setCantidadInstrucciones(int cantidad){
		this.cantidadInstrucciones = cantidad;
	}
    
    // El valor actual del objeto se le suma con el argumento, asegurase que 
    // su argumento sea una suma del valor actual y el valor agregado
    public void setTiempoCola(int tiempoCola) { this.tiempoCola = tiempoCola; }
    public void setTiempoBloqueo(int tiempoBloqueo) { this.tiempoBloqueo = tiempoBloqueo; }
    public void setTiempoEjecucion(int tiempoEjecucion) { this.tiempoEjecucion = tiempoEjecucion; }
    public void setTiempoTotal(int tiempoTotal) { this.tiempoTotal = tiempoTotal; }
}
