/*
 * ITurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:59
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public interface ITurnoAluno extends IDomainObject {
    public IShift getShift();

    public IStudent getStudent();

    public void setShift(IShift turno);

    public void setStudent(IStudent aluno);
}