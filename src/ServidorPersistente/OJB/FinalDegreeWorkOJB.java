/*
 * Created on 2004/03/09
 *  
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.finalDegreeWork.Group;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Proposal;
import Dominio.finalDegreeWork.Scheduleing;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import Util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 */

public class FinalDegreeWorkOJB
	extends ObjectFenixOJB
	implements IPersistentFinalDegreeWork {

	public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		return queryList(Proposal.class, criteria);
	}

	public IScheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		return (IScheduleing) queryObject(Scheduleing.class, criteria);
	}

	public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("orientator.idInternal", teacherOID);
		Criteria criteriaCorientator = new Criteria();
		criteriaCorientator.addEqualTo("coorientator.idInternal", teacherOID);
		criteria.addOrCriteria(criteriaCorientator);
		return queryList(Proposal.class, criteria);
	}

	public List readAprovedFinalDegreeWorkProposals(Integer executionDegreeOID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		criteria.addEqualTo("status", FinalDegreeWorkProposalStatus.APPROVED_STATUS);
		return queryList(Proposal.class, criteria);
	}

	public List readPublishedFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		criteria.addEqualTo("status", FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
		return queryList(Proposal.class, criteria);
	}

	public IGroup readFinalDegreeWorkGroupByUsername(String username)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("groupStudents.student.person.username", username);
		return (IGroup) queryObject(Group.class, criteria);
	}

}