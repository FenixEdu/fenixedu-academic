package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.CreditLineType;

/**
 * @author Fernanda Quitério 28/Nov/2003
 *  
 */
public interface ICreditLine extends IDomainObject {
    public Double getCredits();

    public void setCredits(Double credits);

    public ITeacher getTeacher();

    public void setTeacher(ITeacher teacher);

    public CreditLineType getType();
}