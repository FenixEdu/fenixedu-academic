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

import DataBeans.InfoMasterDegreeCandidate;
import Dominio.ICountry;
import Dominio.ICurso;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;

public class ChangeMasterDegreeCandidate implements IServico {
    
    private static ChangeMasterDegreeCandidate servico = new ChangeMasterDegreeCandidate();
    
    /**
     * The singleton access method of this class.
     **/
    public static ChangeMasterDegreeCandidate getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ChangeMasterDegreeCandidate() { 
    }
    
    /**
     * Returns the service name
     **/
    
    public final String getNome() {
        return "ChangeMasterDegreeCandidate";
    }
    
    
    public void run(InfoMasterDegreeCandidate newMasterDegreeCandidate) 
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate existingMasterDegreeCandidate = null;

        try {
	        sp = SuportePersistenteOJB.getInstance();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

        try {
            ICurso degreeTemp = sp.getICursoPersistente().readBySigla(newMasterDegreeCandidate.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
            existingMasterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(newMasterDegreeCandidate.getCandidateNumber(), 
            								newMasterDegreeCandidate.getInfoExecutionDegree().getInfoExecutionYear().getYear(),
            								degreeTemp.getSigla(),
											new Specialization(newMasterDegreeCandidate.getSpecialization()));
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 
		if (existingMasterDegreeCandidate == null)
			throw new ExcepcaoInexistente("Unknown Candidate !!");	

		// Get new Country
		ICountry country = null;
		ICountry nationality = null;
        try {
			country = sp.getIPersistentCountry().readCountryByName(newMasterDegreeCandidate.getInfoPerson().getInfoPais().getName());
			nationality = sp.getIPersistentCountry().readCountryByNationality(newMasterDegreeCandidate.getInfoPerson().getNacionalidade());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 
        
        
        // FIXME Nuno Nunes: Ver com os roles o que e preciso Mudar
	    // Change the Master Degree Candidate Personal Information 
//		existingMasterDegreeCandidate.changePersonalData(newMasterDegreeCandidate.getName(), 
//								newMasterDegreeCandidate.getMajorDegree(), 
//								newMasterDegreeCandidate.getMajorDegreeSchool(),
//								newMasterDegreeCandidate.getMajorDegreeYear(), 
//								newMasterDegreeCandidate.getFatherName(), 
//								newMasterDegreeCandidate.getMotherName(), 
//								newMasterDegreeCandidate.getBirthPlaceParish(),
//								newMasterDegreeCandidate.getBirthPlaceMunicipality(), 
//								newMasterDegreeCandidate.getBirthPlaceDistrict(), 
//								newMasterDegreeCandidate.getIdentificationDocumentNumber(), 
//								newMasterDegreeCandidate.getIdentificationDocumentIssuePlace(), 
//								newMasterDegreeCandidate.getAddress(), 
//								newMasterDegreeCandidate.getPlace(), 
//								newMasterDegreeCandidate.getPostCode(), 
//								newMasterDegreeCandidate.getAddressParish(), 
//								newMasterDegreeCandidate.getAddressMunicipality(), 
//								newMasterDegreeCandidate.getAddressDistrict(), 
//								newMasterDegreeCandidate.getTelephone(), 
//								newMasterDegreeCandidate.getMobilePhone(), 
//								newMasterDegreeCandidate.getEmail(), 
//								newMasterDegreeCandidate.getWebSite(), 
//								newMasterDegreeCandidate.getContributorNumber(), 
//								newMasterDegreeCandidate.getOccupation(), 
//								newMasterDegreeCandidate.getSex(), 
//								newMasterDegreeCandidate.getInfoIdentificationDocumentType(),
//								newMasterDegreeCandidate.getMaritalStatus(),
//								country,
//								nationality, 
//								newMasterDegreeCandidate.getSpecialization(), 
//							    newMasterDegreeCandidate.getAverage(), 
//							    newMasterDegreeCandidate.getBirth(), 
//							    newMasterDegreeCandidate.getIdentificationDocumentIssueDate(),
//							    newMasterDegreeCandidate.getIdentificationDocumentExpirationDate()); 

		try {
            sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(existingMasterDegreeCandidate);
	    } catch (ExcepcaoPersistencia ex) {
	      FenixServiceException newEx = new FenixServiceException("Persistence layer error");
	      newEx.fillInStackTrace();
	      throw newEx;
	    }
    }
}