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
    private Double credits;
    /**
     * @return Returns the credits.
     */
    public Double getCredits()
    {
        return credits;
    }

    /**
     * @param credits The credits to set.
     */
    public void setCredits(Double credits)
    {
        this.credits = credits;
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
