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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import Util.CandidateSituationValidation;

public class CandidateSituationOJB extends ObjectFenixOJB implements IPersistentCandidateSituation{
    
    
    /** Creates a new instance of CandidateSituationOJB */
    public CandidateSituationOJB() {
    }
    
    public ICandidateSituation readActiveCandidateSituation(Integer candidateNumber, String degreeCode, String applicationYear) throws ExcepcaoPersistencia {
        try {
            ICandidateSituation candidate = null;
            
            String oqlQuery = "select all from " + CandidateSituation.class.getName()
            + " where masterDegreeCandidate.candidateNumber = $1"
            + " and masterDegreeCandidate.executionDegree.curricularPlan.curso.sigla = $2"
            + " and masterDegreeCandidate.executionDegree.executionYear.year = $3"
            + " and validation = $4";
            
            query.create(oqlQuery);
			query.bind(candidateNumber);
			query.bind(degreeCode);
			query.bind(applicationYear);
			query.bind(new Integer(CandidateSituationValidation.ACTIVE));
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                candidate = (ICandidateSituation) result.get(0);
            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public List readCandidateSituations(Integer candidateNumber, String degreeCode, String applicationYear) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CandidateSituation.class.getName()
			+ " where masterDegreeCandidate.candidateNumber = $1"
			+ " and masterDegreeCandidate.executionDegree.curricularPlan.curso.sigla = $2"
			+ " and masterDegreeCandidate.executionDegree.executionYear.year = $3";
            
			query.create(oqlQuery);
			query.bind(candidateNumber);
			query.bind(degreeCode);
			query.bind(applicationYear);
            
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



