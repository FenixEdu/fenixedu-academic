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

import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;

public class MasterDegreeCandidateOJB extends ObjectFenixOJB implements IPersistentMasterDegreeCandidate{
    
    
    /** Creates a new instance of MasterDegreeCandidateOJB */
    public MasterDegreeCandidateOJB() {
    }
    
    public IMasterDegreeCandidate readMasterDegreeCandidateByUsername(String username) throws ExcepcaoPersistencia {
        try {
            IMasterDegreeCandidate candidate = null;

            String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName();
            oqlQuery += " where person.username = $1 ";
			
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
    
    
    public IMasterDegreeCandidate readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(Integer candidateNumber, String applicationYear, String degreeCode,
    	   Specialization specialization) throws ExcepcaoPersistencia {
        try {
            IMasterDegreeCandidate candidate = null;
            
            String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
		            + " where candidateNumber = $1"
		            + " and executionDegree.executionYear.year = $2"
					+ " and executionDegree.curricularPlan.degree.sigla = $3" 
					+ " and specialization = $4" ;

            query.create(oqlQuery);
			query.bind(candidateNumber);
			query.bind(applicationYear);
			query.bind(degreeCode);
			query.bind(specialization.getSpecialization());

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
    	
    	IMasterDegreeCandidate masterDegreeCandidateBD1 = this.readMasterDegreeCandidateByUsername(masterDegreeCandidateToWrite.getPerson().getUsername());
    	
		IMasterDegreeCandidate masterDegreeCandidateBD2 = this.readByIdentificationDocNumberAndTypeAndExecutionDegree(
				masterDegreeCandidateToWrite.getPerson().getNumeroDocumentoIdentificacao(),
				masterDegreeCandidateToWrite.getPerson().getTipoDocumentoIdentificacao().getTipo(),
				masterDegreeCandidateToWrite.getExecutionDegree());
    	
    	if (masterDegreeCandidateBD1 == null && masterDegreeCandidateBD2 == null) {   	
        	super.lockWrite(masterDegreeCandidateToWrite);
        	return;
    	}
        	
		if (masterDegreeCandidateBD1 != null &&
				 (masterDegreeCandidateToWrite instanceof MasterDegreeCandidate) &&
				 ((MasterDegreeCandidate) masterDegreeCandidateBD1).getInternalCode().equals(
				 ((MasterDegreeCandidate) masterDegreeCandidateToWrite).getInternalCode())) {
				   super.lockWrite(masterDegreeCandidateToWrite);
				   return;
				 }
		if (masterDegreeCandidateBD2 != null &&
			    (masterDegreeCandidateToWrite instanceof MasterDegreeCandidate) &&
				((MasterDegreeCandidate) masterDegreeCandidateBD2).getInternalCode().equals(
				((MasterDegreeCandidate) masterDegreeCandidateToWrite).getInternalCode())) {
				    super.lockWrite(masterDegreeCandidateToWrite);
				    return;
		} else
			throw new ExistingPersistentException();
        
    }
    
    
	public Integer generateCandidateNumber(String executionYear, String degreeName) throws ExcepcaoPersistencia {
		try {
			int number = 0;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
							+ " where executionDegree.executionYear.year = $1"
							+ " and executionDegree.curricularPlan.degree.nome = $2"
							+ " order by candidateNumber desc";
			query.create(oqlQuery);
			query.bind(executionYear);
			query.bind(degreeName);
			List result = (List) query.execute();

			lockRead(result);
			if (result.size() != 0)
				number = ((IMasterDegreeCandidate) result.get(0)).getCandidateNumber().intValue();

			return new Integer(number + 1);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
    public void delete(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
        super.delete(masterDegreeCandidate);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    
	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegree(String idDocumentNumber, Integer idDocumentType, 
				 ICursoExecucao executionDegree) throws ExcepcaoPersistencia {
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where person.numeroDocumentoIdentificacao = $1"
					+ " and person.tipoDocumentoIdentificacao = $2"
					+ " and executionDegree.executionYear.year = $3" 
					+ " and executionDegree.curricularPlan.name = $4" 
					+ " and executionDegree.curricularPlan.degree.nome = $5" ;
	
			query.create(oqlQuery);
			query.bind(idDocumentNumber);
			query.bind(idDocumentType);
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());
	
			List result = (List) query.execute();
	
			lockRead(result);
			if (result.size() != 0)
				candidate = (IMasterDegreeCandidate) result.get(0);
	
			return candidate;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
} // End of class definition
