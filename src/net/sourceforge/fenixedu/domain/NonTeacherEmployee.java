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
        this.setIdInternal(0);
        this.setChaveFuncionario(0);
    }

    public NonTeacherEmployee(int codigoInterno, int chaveFuncionario) {
        this.setIdInternal(codigoInterno);
        this.setChaveFuncionario(chaveFuncionario);
    }

    public boolean equals(Object obj) {
        if (obj instanceof INonTeacherEmployee) {
            final INonTeacherEmployee nonTeacherEmployee = (INonTeacherEmployee) obj;
            return this.getIdInternal().equals(nonTeacherEmployee.getIdInternal());
        }
        return false;
    }

}
