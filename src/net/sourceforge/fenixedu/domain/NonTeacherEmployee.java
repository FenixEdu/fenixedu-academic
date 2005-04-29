/*
 * NonTeacherEmployee.java
 *
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Ivo Brandão
 */
public class NonTeacherEmployee extends NonTeacherEmployee_Base {

    public NonTeacherEmployee() {
        setIdInternal(0);
        setChaveFuncionario(0);
    }

    public NonTeacherEmployee(int codigoInterno, int chaveFuncionario) {
        setIdInternal(codigoInterno);
        setChaveFuncionario(chaveFuncionario);
    }

    public boolean equals(Object obj) {
        return ((obj instanceof NonTeacherEmployee)
                && (getIdInternal() == ((NonTeacherEmployee) obj).getIdInternal()) && (getChaveFuncionario() == ((NonTeacherEmployee) obj)
                .getChaveFuncionario()));
    }

}