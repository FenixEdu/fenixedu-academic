/*
 * DepartmentCourse.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * 
 * @author dcs-rjao
 */

package Dominio;

public class DepartmentCourse extends DomainObject implements IDepartmentCourse {

    private Integer codigoInterno;

    private Integer chaveDepartamento;

    private String nome;

    private String sigla;

    private IDepartment departamento;

    public DepartmentCourse() {
        setNome("");
        setSigla("");
        setDepartamento(null);
    }

    public DepartmentCourse(String nome, String sigla, IDepartment departamento) {
        setCodigoInterno(null);
        setNome(nome);
        setSigla(sigla);
        setDepartamento(departamento);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof DepartmentCourse) {
            DepartmentCourse d = (DepartmentCourse) obj;
            resultado = (getNome().equals(d.getNome()) && getSigla().equals(d.getSigla()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[DISCIPLINA_DEPARTAMENTO";
        result += ", codInt=" + codigoInterno;
        result += ", sigla=" + sigla;
        result += ", nome=" + nome;
        result += ", departamento=" + departamento;
        result += "]";
        return result;
    }

    /**
     * Getter for property codigoInterno.
     * 
     * @return Value of property codigoInterno.
     *  
     */
    public java.lang.Integer getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * Setter for property codigoInterno.
     * 
     * @param codigoInterno
     *            New value of property codigoInterno.
     *  
     */
    public void setCodigoInterno(java.lang.Integer codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * Getter for property nome.
     * 
     * @return Value of property nome.
     *  
     */
    public java.lang.String getNome() {
        return nome;
    }

    /**
     * Setter for property nome.
     * 
     * @param nome
     *            New value of property nome.
     *  
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    /**
     * Getter for property sigla.
     * 
     * @return Value of property sigla.
     *  
     */
    public java.lang.String getSigla() {
        return sigla;
    }

    /**
     * Setter for property sigla.
     * 
     * @param sigla
     *            New value of property sigla.
     *  
     */
    public void setSigla(java.lang.String sigla) {
        this.sigla = sigla;
    }

    /**
     * Getter for property chaveDepartamento.
     * 
     * @return Value of property chaveDepartamento.
     *  
     */
    public java.lang.Integer getChaveDepartamento() {
        return chaveDepartamento;
    }

    /**
     * Setter for property chaveDepartamento.
     * 
     * @param chaveDepartamento
     *            New value of property chaveDepartamento.
     *  
     */
    public void setChaveDepartamento(java.lang.Integer chaveDepartamento) {
        this.chaveDepartamento = chaveDepartamento;
    }

    /**
     * Returns the departamento.
     * 
     * @return IDepartment
     */
    public IDepartment getDepartamento() {
        return departamento;
    }

    /**
     * Sets the departamento.
     * 
     * @param departamento
     *            The departamento to set
     */
    public void setDepartamento(IDepartment departamento) {
        this.departamento = departamento;
    }

}