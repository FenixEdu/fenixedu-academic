/*
 * FuncNaoDocente.java
 *
 */

package Dominio;

/**
 * 
 * @author Ivo Brandão
 */
public class FuncNaoDocente extends DomainObject {

    private int codigoInterno = 0;

    private int chaveFuncionario = 0;

    private Funcionario funcionario = null;

    /** Construtor por defeito */
    public FuncNaoDocente() {
        this.codigoInterno = 0;
        this.chaveFuncionario = 0;
    }

    /** Construtor */
    public FuncNaoDocente(int codigoInterno, int chaveFuncionario) {
        this.codigoInterno = codigoInterno;
        this.chaveFuncionario = chaveFuncionario;
    }

    /** Verifica se outro objecto e identico a este */
    public boolean equals(Object obj) {

        return ((obj instanceof FuncNaoDocente)
                && (codigoInterno == ((FuncNaoDocente) obj).getCodigoInterno()) && (chaveFuncionario == ((FuncNaoDocente) obj)
                .getChaveFuncionario()));
    }

    /**
     * Getter for property codigoInterno.
     * 
     * @return Value of property codigoInterno.
     *  
     */
    public int getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * Setter for property codigoInterno.
     * 
     * @param codigoInterno
     *            New value of property codigoInterno.
     *  
     */
    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * Getter for property chaveFuncionario.
     * 
     * @return Value of property chaveFuncionario.
     *  
     */
    public int getChaveFuncionario() {
        return chaveFuncionario;
    }

    /**
     * Setter for property chaveFuncionario.
     * 
     * @param chaveFuncionario
     *            New value of property chaveFuncionario.
     *  
     */
    public void setChaveFuncionario(int chaveFuncionario) {
        this.chaveFuncionario = chaveFuncionario;
    }

    /**
     * @return
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}