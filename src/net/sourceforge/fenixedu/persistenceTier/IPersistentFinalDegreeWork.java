/*
 * Created on 2004/03/08
 *  
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;

/**
 * @author Luis Cruz
 */

public interface IPersistentFinalDegreeWork extends IPersistentObject {

    public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public IScheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID) throws ExcepcaoPersistencia;

    public List readAprovedFinalDegreeWorkProposals(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public List readPublishedFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public IGroup readFinalDegreeWorkGroupByUsername(String username) throws ExcepcaoPersistencia;

    public IProposal readFinalDegreeWorkAttributedToGroupByTeacher(Integer groupOid)
            throws ExcepcaoPersistencia;

}