/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public interface IDelegate extends IDomainObject {
    public IDegree getDegree();

    public void setDegree(IDegree degree);

    public IExecutionYear getExecutionYear();

    public void setExecutionYear(IExecutionYear executionYear);

    public IStudent getStudent();

    public void setStudent(IStudent student);

    public DelegateYearType getYearType();

    public void setYearType(DelegateYearType yearType);

    public Boolean getType();

    public void setType(Boolean type);
}