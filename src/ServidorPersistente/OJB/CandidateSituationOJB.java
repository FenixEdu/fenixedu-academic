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

public class CandidateSituationOJB extends ObjectFenixOJB implements IPersistentCandidateSituation{
    
    
    /** Creates a new instance of CandidateSituationOJB */
    public CandidateSituationOJB() {
    }
    
    public ICandidateSituation readCandidateSituation(Integer candidateNumber, String degreeCode, Integer applicationYear) throws ExcepcaoPersistencia {
        try {
            ICandidateSituation candidate = null;
            
            String oqlQuery = "select all from " + CandidateSituation.class.getName()
            + " where masterDegreeCandidate.candidateNumber = $1"
            + " and masterDegreeCandidate.degree.sigla = $2"
            + " and masterDegreeCandidate.applicationYear = $3";
            
            query.create(oqlQuery);
			query.bind(candidateNumber);
			query.bind(degreeCode);
			query.bind(applicationYear);
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                candidate = (ICandidateSituation) result.get(0);
            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void writeCandidateSituation(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia {
        super.lockWrite(candidateSituation);        
    }
    
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia {
        super.delete(candidateSituation);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + CandidateSituation.class.getName();
        super.deleteAll(oqlQuery);
    }    
    
} // End of class definition



