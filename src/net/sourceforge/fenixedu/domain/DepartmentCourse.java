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
        result += ", departamento=" + getDepartamento();
        result += "]";
        return result;
    }

}