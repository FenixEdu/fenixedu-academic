/*
 * Created on Mar 10, 2004
 */
package Dominio.finalDegreeWork;

import java.util.Date;

import Dominio.ICursoExecucao;
import Dominio.IDomainObject;

/**
 * @author Luis Cruz
 */
public interface IScheduleing extends IDomainObject {

    public ICursoExecucao getExecutionDegree();

    public void setExecutionDegree(ICursoExecucao executionDegree);

    public Date getEndOfProposalPeriod();

    public void setEndOfProposalPeriod(Date endOfProposalPeriod);

    public Date getStartOfProposalPeriod();

    public void setStartOfProposalPeriod(Date startOfProposalPeriod);

    public Integer getCurrentProposalNumber();

    public void setCurrentProposalNumber(Integer currentProposalNumber);

    public Date getStartOfCandidacyPeriod();

    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod);

    public Date getEndOfCandidacyPeriod();

    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod);

    public Integer getMinimumNumberOfCompletedCourses();

    public void setMinimumNumberOfCompletedCourses(Integer minimumNumberOfCompletedCourses);

    public Integer getMaximumNumberOfStudents();

    public void setMaximumNumberOfStudents(Integer maximumNumberOfStudents);

    public Integer getMinimumNumberOfStudents();

    public void setMinimumNumberOfStudents(Integer minimumNumberOfStudents);

    public Integer getMaximumNumberOfProposalCandidaciesPerGroup();

    public void setMaximumNumberOfProposalCandidaciesPerGroup(
            Integer maximumNumberOfProposalCandidaciesPerGroup);

}