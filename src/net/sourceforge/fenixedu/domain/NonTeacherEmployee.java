/*
 * NonTeacherEmployee.java
 *
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Ivo Brandão
 */
public class NonTeacherEmployee extends DomainObject {

    private int codigoInterno = 0;

    private int chaveFuncionario = 0;

    private Funcionario funcionario = null;

    /** Construtor por defeito */
    public NonTeacherEmployee() {
        this.codigoInterno = 0;
        this.chaveFuncionario = 0;
    }

    /** Construtor */
    public NonTeacherEmployee(int codigoInterno, int chaveFuncionario) {
        this.codigoInterno = codigoInterno;
        this.chaveFuncionario = chaveFuncionario;
    }

    /** Verifica se outro objecto e identico a este */
    public boolean equals(Object obj) {

        return ((obj instanceof NonTeacherEmployee)
                && (codigoInterno == ((NonTeacherEmployee) obj).getCodigoInterno()) && (chaveFuncionario == ((NonTeacherEmployee) obj)
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