/*
 * CandidateSituationOJB.java
 *
 * Created on 1 de Novembro de 2002, 16:13
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import Util.State;

public class CandidateSituationOJB extends ObjectFenixOJB implements IPersistentCandidateSituation{
    
    
    /** Creates a new instance of CandidateSituationOJB */
    public CandidateSituationOJB() {
    }
    
    public ICandidateSituation readActiveCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
        try {
            ICandidateSituation candidate = null;
            
            String oqlQuery = "select all from " + CandidateSituation.class.getName()
				            + " where masterDegreeCandidate.candidateNumber = $1"
				            + " and masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla = $2"
				            + " and masterDegreeCandidate.executionDegree.executionYear.year = $3"
							+ " and validation = $4"
							+ " and masterDegreeCandidate.specialization = $5";
            
            query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getCandidateNumber());
			query.bind(masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
			query.bind(masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
			query.bind(new Integer(State.ACTIVE));
			query.bind(masterDegreeCandidate.getSpecialization().getSpecialization());
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                candidate = (ICandidateSituation) result.get(0);
            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public List readCandidateSituations(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CandidateSituation.class.getName()
							+ " where masterDegreeCandidate.candidateNumber = $1"
							+ " and masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla = $2"
							+ " and masterDegreeCandidate.executionDegree.executionYear.year = $3"
							+ " and masterDegreeCandidate.specialization = $4";
            
			query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getCandidateNumber());
			query.bind(masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
			query.bind(masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
			query.bind(masterDegreeCandidate.getSpecialization().getSpecialization());
            
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
    public void writeCandidateSituation(ICandidateSituation candidateSituationToWrite) throws ExcepcaoPersistencia {
		super.lockWrite(candidateSituationToWrite);
    }
    
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia {
        super.delete(candidateSituation);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + CandidateSituation.class.getName();
        super.deleteAll(oqlQuery);
    }    
    
} // End of class definition



