/*
 * DiaSemana.java
 *
 * Created on 11 de Outubro de 2002, 17:16
 */

package Util;


/**
 *
 * @author  tfc130
 */
public class TipoSala extends FenixUtil {
    public static final int ANFITEATRO = 1;
    public static final int LABORATORIO = 2;
    public static final int PLANA = 3;
    
    private Integer tipo;    
    
    public TipoSala() {
    }
    
    public TipoSala(int tipo_sala) {
        this.tipo = new Integer(tipo_sala);
    }

    public TipoSala(Integer tipo_sala) {
        this.tipo = tipo_sala;
    }

    public Integer getTipo() {
        return this.tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = new Integer(tipo);
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof TipoSala) {
            TipoSala tipo = (TipoSala)obj;
            resultado = (getTipo().equals(tipo.getTipo()));
        }
        return resultado;    
    }   
    
	public String toString() {
		int value = this.tipo.intValue();
		switch (value) {
			case ANFITEATRO : return "A";
			case LABORATORIO : return "L";
			case PLANA : return "P";
		}
		return "Error: Invalid lesson type";
	}
}