/*
 * DepartmentCourse.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * 
 * @author dcs-rjao
 */

package net.sourceforge.fenixedu.domain;

public class DepartmentCourse extends DepartmentCourse_Base {

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
        result += ", codInt=" + getCodigoInterno();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", departamento=" + departamento;
        result += "]";
        return result;
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