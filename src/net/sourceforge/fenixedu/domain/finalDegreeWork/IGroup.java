/*
 * Created on Apr 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author Luis Cruz
 *  
 */
public interface IGroup extends IDomainObject {

    public IExecutionDegree getExecutionDegree();

    public void setExecutionDegree(IExecutionDegree executionDegree);

    public List getGroupStudents();

    public void setGroupStudents(List groupStudents);

    public List getGroupProposals();

    public void setGroupProposals(List groupProposals);

}