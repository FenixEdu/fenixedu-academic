/*
 * MasterDegreeCandidateOJB.java
 *
 * Created on 17 de Outubro de 2002, 11:30
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

import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class MasterDegreeCandidateOJB extends ObjectFenixOJB implements IPersistentMasterDegreeCandidate{
    
    
    /** Creates a new instance of MasterDegreeCandidateOJB */
    public MasterDegreeCandidateOJB() {
    }
    
    public IMasterDegreeCandidate readMasterDegreeCandidateByUsername(String username) throws ExcepcaoPersistencia {
        try {
            IMasterDegreeCandidate candidate = null;

            String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName();
            oqlQuery += " where username = $1 ";

            query.create(oqlQuery);
            query.bind(username);
            List result = (List) query.execute();
            lockRead(result);
            
            if (result.size() != 0)
                candidate = (IMasterDegreeCandidate) result.get(0);
                
            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    
    public IMasterDegreeCandidate readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(Integer candidateNumber, Integer applicationYear, String degreeCode) throws ExcepcaoPersistencia {
        try {
            IMasterDegreeCandidate candidate = null;
            
            String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
		            + " where candidateNumber = $1"
		            + " and applicationYear = $2"
		            + " and degree.sigla = $3" ;

            query.create(oqlQuery);
			query.bind(candidateNumber);
			query.bind(applicationYear);
			query.bind(degreeCode);

            List result = (List) query.execute();
            
            lockRead(result);
            if (result.size() != 0)
                candidate = (IMasterDegreeCandidate) result.get(0);

            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    
    public void writeMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidateToWrite) throws ExcepcaoPersistencia {
    	if (masterDegreeCandidateToWrite == null) return;
    	
    	IMasterDegreeCandidate masterDegreeCandidateBD = this.readMasterDegreeCandidateByUsername(masterDegreeCandidateToWrite.getUsername());
    	
    	if (masterDegreeCandidateBD == null)    	
        	super.lockWrite(masterDegreeCandidateToWrite);
        	
		else if ((masterDegreeCandidateToWrite instanceof MasterDegreeCandidate) &&
				 ((MasterDegreeCandidate) masterDegreeCandidateBD).getInternalCode().equals(
				 ((MasterDegreeCandidate) masterDegreeCandidateToWrite).getInternalCode())) {
				 super.lockWrite(masterDegreeCandidateToWrite);
		} else
			throw new ExistingPersistentException();
        
    }
    
    public void delete(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
        super.delete(masterDegreeCandidate);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName();
        super.deleteAll(oqlQuery);
    }    
} // End of class definition
