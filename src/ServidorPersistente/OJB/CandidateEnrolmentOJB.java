package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.CandidateEnrolment;
import Dominio.ICandidateEnrolment;
import Dominio.ICurricularCourseScope;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateEnrolment;
import ServidorPersistente.exceptions.ExistingPersistentException;

/*
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolmentOJB extends ObjectFenixOJB implements IPersistentCandidateEnrolment {

	public void delete(ICandidateEnrolment candidateEnrolment) throws ExcepcaoPersistencia {
		super.delete(candidateEnrolment); 	
	}
	

	public List readByMDCandidate(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia{
		
		try {
			String oqlQuery = "select all from " + CandidateEnrolment.class.getName()
							+ " where masterDegreeCandidate.idInternal = $1";
							
			query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getIdInternal());
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ICandidateEnrolment readByMDCandidateAndCurricularCourseScope(IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia{
		
		try {
			String oqlQuery = "select all from " + CandidateEnrolment.class.getName()
							+ " where masterDegreeCandidate.idInternal = $1"
							+ " and curricularCourseScope.idInternal = $2";
						
			query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getIdInternal());
			query.bind(curricularCourseScope.getIdInternal());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() > 0){
				return (ICandidateEnrolment) result.get(0);	
			}
			return null;
			
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public void write(ICandidateEnrolment candidateEnrolment2Write) throws ExcepcaoPersistencia {
		ICandidateEnrolment candidateEnrolmentFromDB = null;
    		
    		
		// If there is nothing to write, simply return.
		if (candidateEnrolment2Write == null){
			return;
		}
		
		candidateEnrolmentFromDB = this.readByMDCandidateAndCurricularCourseScope(candidateEnrolment2Write.getMasterDegreeCandidate(), candidateEnrolment2Write.getCurricularCourseScope());
		
		if (candidateEnrolmentFromDB == null){

			super.lockWrite(candidateEnrolment2Write);
			return;
		}
		if ((candidateEnrolment2Write instanceof CandidateEnrolment) &&
				 ((CandidateEnrolment) candidateEnrolmentFromDB).getIdInternal().equals(
					((CandidateEnrolment) candidateEnrolment2Write).getIdInternal())) {
						super.lockWrite(candidateEnrolment2Write);
						return;	
		} 
		throw new ExistingPersistentException();
	}
	
	public void deleteAllByCandidateID(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia{
		try {
			List result = this.readByMDCandidate(masterDegreeCandidate);
			Iterator iterator = result.iterator();
			while(iterator.hasNext()){
				delete((ICandidateEnrolment) iterator.next());	
			}
			
		} catch(ExcepcaoPersistencia e){
			throw new ExcepcaoPersistencia();
		}
	}
}