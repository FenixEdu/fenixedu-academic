/*
 * ISchoolClassShift.java
 *
 * Created on 19 de Outubro de 2002, 14:39
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public interface ISchoolClassShift extends IDomainObject {
    public ISchoolClass getTurma();

    public IShift getTurno();

    public void setTurma(ISchoolClass turma);

    public void setTurno(IShift turno);
}