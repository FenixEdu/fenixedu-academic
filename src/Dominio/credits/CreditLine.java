/*
 * Created on 29/Fev/2004
 */
package Dominio.credits;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public abstract class CreditLine extends DomainObject implements ICreditLine
{
    private ITeacher teacher;
    /**
     * @param idInternal
     */
    public CreditLine(Integer idInternal)
    {
        super(idInternal);
    }

    /**
     * 
     */
    public CreditLine()
    {
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher()
    {
        return teacher;
    }

    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }

}
