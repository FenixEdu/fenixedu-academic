/*
 * TipoAlunoL.java
 *
 * Created on 21 de Novembro de 2002, 15:43
 */

package Util;

/**
 *
 * @author  Ricardo Nortadas
 */
public class TipoAlunoL {

    public static final int BASE = 1;
    public static final int INSCRITO = 2;
    public static final int PRESCRITO = 3;
    public static final int INTERRUPCAO = 4;
    public static final int AUSENTE = 5;
    public static final int LICENCIADO = 6;
    
    private Integer tipoAlunoL;

    /** Creates a new instance of TipoAlunoL */
    public TipoAlunoL() {
    }
    
    public TipoAlunoL(int tipoAlunoL) {
        this.tipoAlunoL = new Integer(tipoAlunoL);
    }

    public TipoAlunoL(Integer tipoAlunoL) {
        this.tipoAlunoL = tipoAlunoL;
    }
    
    /** Getter for property tipoAlunoL.
     * @return Value of property tipoAlunoL.
     *
     */
    public java.lang.Integer getTipoAlunoL() {
        return tipoAlunoL;
    }
    
    /** Setter for property tipoAlunoL.
     * @param tipoAlunoL New value of property tipoAlunoL.
     *
     */
    public void setTipoAlunoL(java.lang.Integer tipoAlunoL) {
        this.tipoAlunoL = tipoAlunoL;
    }

    public boolean equals(Object o) {
        if(o instanceof TipoAlunoL) {
            TipoAlunoL aux = (TipoAlunoL) o;
            return this.tipoAlunoL.equals(aux.getTipoAlunoL());
        }
        else {
            return false;
        }
    }
}
