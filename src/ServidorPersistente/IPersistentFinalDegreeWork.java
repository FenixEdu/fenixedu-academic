/*
 * Created on 2004/03/08
 *  
 */

package ServidorPersistente;

import java.util.List;

import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IScheduleing;

/**
 * @author Luis Cruz
 */

public interface IPersistentFinalDegreeWork extends IPersistentObject {

	public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
		throws ExcepcaoPersistencia;

	public IScheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
		throws ExcepcaoPersistencia;

	public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID)
		throws ExcepcaoPersistencia;

	public List readAprovedFinalDegreeWorkProposals(Integer executionDegreeOID)
		throws ExcepcaoPersistencia;

	public List readPublishedFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
		throws ExcepcaoPersistencia;

	public IGroup readFinalDegreeWorkGroupByUsername(String username)
		throws ExcepcaoPersistencia;

}
