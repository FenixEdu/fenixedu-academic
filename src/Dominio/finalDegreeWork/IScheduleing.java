/*
 * Created on Mar 10, 2004
 *
 */
package Dominio.finalDegreeWork;

import java.util.Date;

import Dominio.ICursoExecucao;
import Dominio.IDomainObject;

/**
 * @author Luis Cruz
 *
 */
public interface IScheduleing extends IDomainObject
{

	public ICursoExecucao getExecutionDegree();

	public void setExecutionDegree(ICursoExecucao executionDegree);

   public Date getEndOfProposalPeriod();

   public void setEndOfProposalPeriod(Date endOfProposalPeriod);

   public Date getStartOfProposalPeriod();

   public void setStartOfProposalPeriod(Date startOfProposalPeriod);

   public Integer getCurrentProposalNumber();
   
   public void setCurrentProposalNumber(Integer currentProposalNumber);
}
