/*
 * ChangeMasterDegreeCandidate.java
 *
 * O Servico ChangeMasterDegreeCandidate altera a informacao de um candidato de Mestrado
 * Nota : E suposto os campos (numeroCandidato, anoCandidatura, chaveCursoMestrado, username)
 *        nao se puderem alterar
 *
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.candidate;

import java.lang.reflect.InvocationTargetException;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;

public class ChangeApplicationInfo implements IServico {
    
    private static ChangeApplicationInfo servico = new ChangeApplicationInfo();
    
    /**
     * The singleton access method of this class.
     **/
    public static ChangeApplicationInfo getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ChangeApplicationInfo() { 
    }
    
    /**
     * Returns the service name
     **/
    
    public final String getNome() {
        return "ChangeApplicationInfo";
    }
    
    
    public void run(InfoMasterDegreeCandidate newMasterDegreeCandidate) 
	    throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate existingMasterDegreeCandidate = null;

        try {
	        sp = SuportePersistenteOJB.getInstance();
	        ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(newMasterDegreeCandidate.getInfoExecutionDegree());
			existingMasterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
			                                newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
											newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao().getTipo(),
											executionDegree,
											new Specialization(newMasterDegreeCandidate.getSpecialization()));
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 
        
		if (existingMasterDegreeCandidate == null)
			throw new ExcepcaoInexistente("Unknown Candidate !!");	

		// Change the Information
		
		sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(existingMasterDegreeCandidate);
		
		existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
		existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
		existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate.getMajorDegreeSchool());
		existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
		
    }
}