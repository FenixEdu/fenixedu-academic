/*
 * ITurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:59
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
public interface ITurnoAluno extends IDomainObject {
    public ITurno getShift();

    public IStudent getStudent();

    public void setShift(ITurno turno);

    public void setStudent(IStudent aluno);
}