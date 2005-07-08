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

    public String toString() {
        String result = "[DISCIPLINA_DEPARTAMENTO";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", departamento=" + getDepartamento();
        result += "]";
        return result;
    }

}
