/*
 * Created on 2004/03/08
 *  
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;

/**
 * @author Luis Cruz
 */

public interface IPersistentFinalDegreeWork extends IPersistentObject {

    public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public Scheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID) throws ExcepcaoPersistencia;

    public List<Proposal> readAprovedFinalDegreeWorkProposals(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public List readPublishedFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia;

    public Group readFinalDegreeWorkGroupByUsername(String username) throws ExcepcaoPersistencia;

    public Proposal readFinalDegreeWorkAttributedToGroupByTeacher(Integer groupOid)
            throws ExcepcaoPersistencia;

}