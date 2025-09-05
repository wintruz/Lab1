

public class PCBP {
    
    protected int id_proceso;
    protected int Cantidad_intruciones;
    protected boolean bloqueo;
    protected String estado;
    protected int tiempo_Cola;
    protected int tiempo_exe;

    public PCBP(int id_proceso,int Cantidad_intruciones,boolean bloqueo,String estado){
        this.id_proceso = id_proceso;
        this.Cantidad_intruciones = Cantidad_intruciones;
        this.bloqueo = bloqueo;
        this.estado = estado;
    }
    
    public void cambiar_estado(String estado){
        this.estado = estado;
    }
}
