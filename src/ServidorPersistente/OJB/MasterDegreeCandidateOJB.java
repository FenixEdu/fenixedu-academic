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

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationName;
import Util.Specialization;
import Util.State;

public class MasterDegreeCandidateOJB extends ObjectFenixOJB implements IPersistentMasterDegreeCandidate{
    
    
    /** Creates a new instance of MasterDegreeCandidateOJB */
    public MasterDegreeCandidateOJB() {
    }
    
    public List readMasterDegreeCandidatesByUsername(String username) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName();
            oqlQuery += " where person.username = $1 ";
			
			query.create(oqlQuery);
            query.bind(username);
            List result = (List) query.execute();
            lockRead(result);
            return result;
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

    
    public void writeMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidateToWrite) throws ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException {
    	if (masterDegreeCandidateToWrite == null) return;
    	
    	
    	// Write the Person first to see if there's no clash
    	
    	try {
	    	SuportePersistenteOJB.getInstance().getIPessoaPersistente().escreverPessoa(masterDegreeCandidateToWrite.getPerson());
		} catch(ExistingPersistentException e) {
			throw new ExistingPersistentException("Existing Person !");
		}
	
    	IMasterDegreeCandidate masterDegreeCandidateBD1 = this.readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
				masterDegreeCandidateToWrite.getCandidateNumber(),
				masterDegreeCandidateToWrite.getExecutionDegree().getExecutionYear().getYear(),
				masterDegreeCandidateToWrite.getExecutionDegree().getCurricularPlan().getDegree().getSigla(),
				masterDegreeCandidateToWrite.getSpecialization());

		IMasterDegreeCandidate masterDegreeCandidateBD2 = this.readByUsernameAndExecutionDegreeAndSpecialization(
				masterDegreeCandidateToWrite.getPerson().getUsername(),
				masterDegreeCandidateToWrite.getExecutionDegree(),
				masterDegreeCandidateToWrite.getSpecialization());
    	
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
				}
			
		throw new ExistingPersistentException();
    }
    
    
	public Integer generateCandidateNumber(String executionYear, String degreeCode, Specialization specialization) throws ExcepcaoPersistencia {
		try {
			int number = 0;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
							+ " where executionDegree.executionYear.year = $1"
							+ " and executionDegree.curricularPlan.degree.sigla = $2"
							+ " and specialization = $3"
							+ " order by candidateNumber desc";
			query.create(oqlQuery);
			query.bind(executionYear);
			query.bind(degreeCode);
			query.bind(specialization.getSpecialization());
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
    
    
	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(String idDocumentNumber, Integer idDocumentType, 
				 ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia {
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where person.numeroDocumentoIdentificacao = $1"
					+ " and person.tipoDocumentoIdentificacao = $2"
					+ " and executionDegree.executionYear.year = $3" 
					+ " and executionDegree.curricularPlan.name = $4" 
					+ " and executionDegree.curricularPlan.degree.nome = $5 " 
					+ " and specialization = $6 "; 
			
			query.create(oqlQuery);
			query.bind(idDocumentNumber);
			query.bind(idDocumentType);
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());
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
    
    
	public IMasterDegreeCandidate readByUsernameAndExecutionDegreeAndSpecialization(String username, ICursoExecucao executionDegree, Specialization specialization) 
	throws ExcepcaoPersistencia {
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where person.username = $1"
					+ " and executionDegree.executionYear.year = $2" 
					+ " and executionDegree.curricularPlan.name = $3" 
					+ " and executionDegree.curricularPlan.degree.nome = $4" 
					+ " and specialization = $5" ;
	
			query.create(oqlQuery);
			query.bind(username);
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());
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
    
	/**
	 * Reads all candidates that with certains properties. The properties are
	 * specified by the arguments of this method. If an argument is
	 * null, then the candidate can have any value concerning that
	 * argument. 
	 * @return a list with all candidates that satisfy the conditions specified by the 
	 * non-null arguments.
	 **/
	public List readCandidateList(String degreeName, Specialization specialization, SituationName candidateSituation, Integer candidateNumber,
				IExecutionYear executionYear) throws ExcepcaoPersistencia {
		
		  if (degreeName == null && specialization == null && candidateSituation == null && candidateNumber == null)
			return readByExecutionYear(executionYear);

		  try {
		  	
			String oqlQuery = new String("select all from ");

			oqlQuery += MasterDegreeCandidate.class.getName() +
			            " where executionDegree.executionYear.year = \"" + executionYear.getYear()+ "\"";
			
			if (degreeName != null) {
		  		oqlQuery += " and executionDegree.curricularPlan.degree.nome = \"" + degreeName + "\"";
			}

			if (specialization != null) {
		  		oqlQuery += " and specialization = \"" + specialization.getSpecialization() + "\"";
			}

			if (candidateNumber != null) {
				oqlQuery += " and candidateNumber = \"" + candidateNumber + "\"";
			}

			if (candidateSituation != null) {
				oqlQuery += " and situations.situation = \"" + candidateSituation.getSituationName() + "\"" + 
				            " and situations.validation = " + State.ACTIVE ; 
			}
			
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		  } catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		  }
	 } 
	 
	public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where executionDegree.executionYear.year = $1" ; 
	
			query.create(oqlQuery);
			query.bind(executionYear.getYear());
	
			List result = (List) query.execute();
	
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
  
	public IMasterDegreeCandidate readByNumberAndExecutionDegreeAndSpecialization(Integer number, ICursoExecucao executionDegree, 
				Specialization specialization) throws ExcepcaoPersistencia{
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where candidateNumber = $1"
					+ " and executionDegree.executionYear.year = $2" 
					+ " and executionDegree.curricularPlan.name = $3" 
					+ " and executionDegree.curricularPlan.degree.nome = $4" 
					+ " and specialization = $5" ;
	
			query.create(oqlQuery);
			query.bind(number);
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());
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


	public IMasterDegreeCandidate readByExecutionDegreeAndPerson(ICursoExecucao executionDegree, IPessoa person) throws ExcepcaoPersistencia{
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where person.username = $1"
					+ " and executionDegree.executionYear.year = $2" 
					+ " and executionDegree.curricularPlan.name = $3" 
					+ " and executionDegree.curricularPlan.degree.nome = $4"; 
	
			query.create(oqlQuery);
			query.bind(person.getUsername());
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
		
		
	public IMasterDegreeCandidate readByExecutionDegreeAndPersonAndNumber(ICursoExecucao executionDegree, IPessoa person, Integer number) throws ExcepcaoPersistencia{
		try {
			IMasterDegreeCandidate candidate = null;
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where person.username = $1"
					+ " and executionDegree.executionYear.year = $2" 
					+ " and executionDegree.curricularPlan.name = $3" 
					+ " and executionDegree.curricularPlan.degree.nome = $4"
					+ " and candidateNumber = $5"; 
	
			query.create(oqlQuery);
			query.bind(person.getUsername());
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());
			query.bind(number);
			
	
			List result = (List) query.execute();
	
			lockRead(result);
			if (result.size() != 0)
				candidate = (IMasterDegreeCandidate) result.get(0);
	
			return candidate;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

		
	public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia{
		try {
			String oqlQuery = "select all from " + MasterDegreeCandidate.class.getName()
					+ " where executionDegree.executionYear.year = $1" 
					+ " and executionDegree.curricularPlan.name = $2" 
					+ " and executionDegree.curricularPlan.degree.nome = $3"; 
	
			query.create(oqlQuery);
			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(executionDegree.getCurricularPlan().getDegree().getNome());

	
			List result = (List) query.execute();
	
			lockRead(result);
			if (result.size() == 0)
				return null;
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}			
    
} // End of class definition
