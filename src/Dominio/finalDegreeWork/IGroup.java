/*
 * Created on Apr 15, 2004
 *
 */
package Dominio.finalDegreeWork;

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IDomainObject;

/**
 * @author Luis Cruz
 *
 */
public interface IGroup extends IDomainObject
{

	public ICursoExecucao getExecutionDegree();

	public void setExecutionDegree(ICursoExecucao executionDegree);

	public List getGroupStudents();

    public void setGroupStudents(List groupStudents);

    public List getGroupProposals();

    public void setGroupProposals(List groupProposals);

}
