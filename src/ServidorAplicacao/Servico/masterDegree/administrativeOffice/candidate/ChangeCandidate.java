/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICountry;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationName;
import Util.State;

public class ChangeCandidate implements IServico {
    
    private static ChangeCandidate servico = new ChangeCandidate();
    
    /**
     * The singleton access method of this class.
     **/
    public static ChangeCandidate getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ChangeCandidate() { 
    }
    
    /**
     * Returns the service name
     **/
    
    public final String getNome() {
        return "ChangeCandidate";
    }
    
    
    public InfoMasterDegreeCandidate run(Integer oldCandidateID, InfoMasterDegreeCandidate newCandidate) 
	    throws ExcepcaoInexistente, FenixServiceException, IllegalAccessException, InvocationTargetException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;

		IMasterDegreeCandidate masterDegreeCandidate = null;
		IPessoa candidatePerson = null;
        try {
	        sp = SuportePersistenteOJB.getInstance();
	        IMasterDegreeCandidate masterDegreeCandidateTemp = new MasterDegreeCandidate();
	        masterDegreeCandidateTemp.setIdInternal(oldCandidateID);
			
			masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(masterDegreeCandidateTemp, true);
	
			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(newCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(), 
						newCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
	
			if (!person.equals(masterDegreeCandidate.getPerson())){
				throw new ExistingServiceException();
			}
			
			if (masterDegreeCandidate == null) {
				throw new ExcepcaoInexistente("Unknown Candidate !!");
			}

			candidatePerson = person;
//			candidatePerson = masterDegreeCandidate.getPerson();
			sp.getIPessoaPersistente().simpleLockWrite(candidatePerson);
			
			candidatePerson.setTipoDocumentoIdentificacao(newCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
			candidatePerson.setNumeroDocumentoIdentificacao(newCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());
					
        } catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);        	
        } 

	
		

		// Get new Country
		ICountry nationality = null;
		if ((newCandidate.getInfoPerson().getInfoPais() != null) && (candidatePerson.getPais() != null)){
			try {
				if (!newCandidate.getInfoPerson().getInfoPais().getNationality().equals(
						candidatePerson.getPais().getNationality())) {
					nationality = sp.getIPersistentCountry().readCountryByNationality(newCandidate.getInfoPerson().getInfoPais().getNationality());
					candidatePerson.setPais(nationality);
				}
			} catch (ExcepcaoPersistencia ex) {
			  FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			  newEx.fillInStackTrace();
			  throw newEx;
			}
		}
		
		// Change personal Information
		candidatePerson.setNascimento(newCandidate.getInfoPerson().getNascimento());
		candidatePerson.setDataEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson().getDataEmissaoDocumentoIdentificacao());		
		candidatePerson.setDataValidadeDocumentoIdentificacao(newCandidate.getInfoPerson().getDataValidadeDocumentoIdentificacao());
		candidatePerson.setLocalEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson().getLocalEmissaoDocumentoIdentificacao());
		candidatePerson.setNome(newCandidate.getInfoPerson().getNome());
		candidatePerson.setSexo(newCandidate.getInfoPerson().getSexo());
		candidatePerson.setEstadoCivil(newCandidate.getInfoPerson().getEstadoCivil());
		candidatePerson.setNomePai(newCandidate.getInfoPerson().getNomePai());
		candidatePerson.setNomeMae(newCandidate.getInfoPerson().getNomeMae());
		candidatePerson.setFreguesiaNaturalidade(newCandidate.getInfoPerson().getFreguesiaNaturalidade());
		candidatePerson.setConcelhoNaturalidade(newCandidate.getInfoPerson().getConcelhoNaturalidade());
		candidatePerson.setDistritoNaturalidade(newCandidate.getInfoPerson().getDistritoNaturalidade());
		candidatePerson.setLocalidadeCodigoPostal(newCandidate.getInfoPerson().getLocalidadeCodigoPostal());
		candidatePerson.setMorada(newCandidate.getInfoPerson().getMorada());						
		candidatePerson.setLocalidade(newCandidate.getInfoPerson().getLocalidade());
		candidatePerson.setCodigoPostal(newCandidate.getInfoPerson().getCodigoPostal());
		candidatePerson.setFreguesiaMorada(newCandidate.getInfoPerson().getFreguesiaMorada());
		candidatePerson.setConcelhoMorada(newCandidate.getInfoPerson().getConcelhoMorada());
		candidatePerson.setDistritoMorada(newCandidate.getInfoPerson().getDistritoMorada());
		candidatePerson.setTelefone(newCandidate.getInfoPerson().getTelefone());
		candidatePerson.setTelemovel(newCandidate.getInfoPerson().getTelemovel());
		candidatePerson.setEmail(newCandidate.getInfoPerson().getEmail());
		candidatePerson.setEnderecoWeb(newCandidate.getInfoPerson().getEnderecoWeb());
		candidatePerson.setNumContribuinte(newCandidate.getInfoPerson().getNumContribuinte());
		candidatePerson.setProfissao(newCandidate.getInfoPerson().getProfissao());
		candidatePerson.setNacionalidade(newCandidate.getInfoPerson().getNacionalidade());
		
		// Change Application Information
		masterDegreeCandidate.setAverage(newCandidate.getAverage());
		masterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
		masterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
		masterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
		masterDegreeCandidate.setSpecializationArea(newCandidate.getSpecializationArea());
		
		
		masterDegreeCandidate.setPerson(candidatePerson);
		
		// Change Situation
		
		ICandidateSituation candidateSituation = null;
		Set situations = new HashSet();
				
				
		if (!masterDegreeCandidate.getActiveCandidateSituation().getSituation().equals(new SituationName(newCandidate.getInfoCandidateSituation().getSituation()))){

			candidateSituation = new CandidateSituation();
			sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
			
			// Change the Active Situation
			Iterator iterator = masterDegreeCandidate.getSituations().iterator();
			while(iterator.hasNext()) {
				ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
				if (candidateSituationTemp.getValidation().equals(new State(State.ACTIVE))){
					sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituationTemp);
					candidateSituationTemp.setValidation(new State(State.INACTIVE));
				}
				situations.add(candidateSituationTemp);
			}
//sp.getIPersistentCandidateSituation().writeCandidateSituation(masterDegreeCandidate.getActiveCandidateSituation());			
//masterDegreeCandidate.getActiveCandidateSituation().setValidation(new State(State.INACTIVE));

			Calendar calendar = Calendar.getInstance();
			candidateSituation.setDate(calendar.getTime());
			candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
			candidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
		
			candidateSituation.setSituation(new SituationName(newCandidate.getInfoCandidateSituation().getSituation()));
			candidateSituation.setValidation(new State(State.ACTIVE));
			try {		
				sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
			} catch (ExcepcaoPersistencia ex) {
			  FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			  newEx.fillInStackTrace();
			  throw newEx;
			}	
			
			situations.add(candidateSituation);
		} else 
			situations.addAll(masterDegreeCandidate.getSituations());
	
			
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		
		List situationsList = new ArrayList();
		Iterator iterator = situations.iterator();
		while(iterator.hasNext()){
			InfoCandidateSituation infoCandidateSituation = Cloner.copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator.next()); 
			situationsList.add(infoCandidateSituation);

			// Check if this is the Active Situation
			if 	(infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
				infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		}

		infoMasterDegreeCandidate.setSituationList(situationsList);

		return infoMasterDegreeCandidate;

    }
}