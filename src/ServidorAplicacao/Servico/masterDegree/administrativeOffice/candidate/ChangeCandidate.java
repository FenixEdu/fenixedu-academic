/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.Iterator;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICountry;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.Specialization;
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
    
    
    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate oldCandidate, InfoMasterDegreeCandidate newCandidate) 
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

		IMasterDegreeCandidate masterDegreeCandidate = null;
        try {
	        sp = SuportePersistenteOJB.getInstance();
			masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
				oldCandidate.getCandidateNumber(),
				oldCandidate.getInfoExecutionDegree().getInfoExecutionYear().getYear(),
				oldCandidate.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
				new Specialization(oldCandidate.getSpecialization()));
			
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 

		if (masterDegreeCandidate == null)
			throw new ExcepcaoInexistente("Unknown Candidate !!");	

		// Get new Country
		ICountry nationality = null;
		try {
			if (!newCandidate.getInfoPerson().getInfoPais().getNationality().equals(
					masterDegreeCandidate.getPerson().getPais().getNationality())) {
				nationality = sp.getIPersistentCountry().readCountryByNationality(newCandidate.getInfoPerson().getInfoPais().getNationality());
				masterDegreeCandidate.getPerson().setPais(nationality);
			}
		} catch (ExcepcaoPersistencia ex) {
		  FenixServiceException newEx = new FenixServiceException("Persistence layer error");
		  newEx.fillInStackTrace();
		  throw newEx;
		}

		// Change personal Information
		masterDegreeCandidate.getPerson().setNascimento(newCandidate.getInfoPerson().getNascimento());
		masterDegreeCandidate.getPerson().setDataEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson().getDataEmissaoDocumentoIdentificacao());		
		masterDegreeCandidate.getPerson().setDataValidadeDocumentoIdentificacao(newCandidate.getInfoPerson().getDataValidadeDocumentoIdentificacao());
		masterDegreeCandidate.getPerson().setTipoDocumentoIdentificacao(newCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
		masterDegreeCandidate.getPerson().setNumeroDocumentoIdentificacao(newCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());			
		masterDegreeCandidate.getPerson().setLocalEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson().getLocalEmissaoDocumentoIdentificacao());
		masterDegreeCandidate.getPerson().setNome(newCandidate.getInfoPerson().getNome());
		masterDegreeCandidate.getPerson().setSexo(newCandidate.getInfoPerson().getSexo());
		masterDegreeCandidate.getPerson().setEstadoCivil(newCandidate.getInfoPerson().getEstadoCivil());
		masterDegreeCandidate.getPerson().setNomePai(newCandidate.getInfoPerson().getNomePai());
		masterDegreeCandidate.getPerson().setNomeMae(newCandidate.getInfoPerson().getNomeMae());
		masterDegreeCandidate.getPerson().setFreguesiaNaturalidade(newCandidate.getInfoPerson().getFreguesiaNaturalidade());
		masterDegreeCandidate.getPerson().setConcelhoNaturalidade(newCandidate.getInfoPerson().getConcelhoNaturalidade());
		masterDegreeCandidate.getPerson().setDistritoNaturalidade(newCandidate.getInfoPerson().getDistritoNaturalidade());
		masterDegreeCandidate.getPerson().setLocalidadeCodigoPostal(newCandidate.getInfoPerson().getLocalidadeCodigoPostal());
		masterDegreeCandidate.getPerson().setMorada(newCandidate.getInfoPerson().getMorada());						
		masterDegreeCandidate.getPerson().setLocalidade(newCandidate.getInfoPerson().getLocalidade());
		masterDegreeCandidate.getPerson().setCodigoPostal(newCandidate.getInfoPerson().getCodigoPostal());
		masterDegreeCandidate.getPerson().setFreguesiaMorada(newCandidate.getInfoPerson().getFreguesiaMorada());
		masterDegreeCandidate.getPerson().setConcelhoMorada(newCandidate.getInfoPerson().getConcelhoMorada());
		masterDegreeCandidate.getPerson().setDistritoMorada(newCandidate.getInfoPerson().getDistritoMorada());
		masterDegreeCandidate.getPerson().setTelefone(newCandidate.getInfoPerson().getTelefone());
		masterDegreeCandidate.getPerson().setTelemovel(newCandidate.getInfoPerson().getTelemovel());
		masterDegreeCandidate.getPerson().setEmail(newCandidate.getInfoPerson().getEmail());
		masterDegreeCandidate.getPerson().setEnderecoWeb(newCandidate.getInfoPerson().getEnderecoWeb());
		masterDegreeCandidate.getPerson().setNumContribuinte(newCandidate.getInfoPerson().getNumContribuinte());
		masterDegreeCandidate.getPerson().setProfissao(newCandidate.getInfoPerson().getProfissao());
		masterDegreeCandidate.getPerson().setNacionalidade(newCandidate.getInfoPerson().getNacionalidade());
		
		// Change Application Information
		masterDegreeCandidate.setAverage(newCandidate.getAverage());
		masterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
		masterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
		masterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
		
		// Change Situation
		
		ICandidateSituation candidateSituation = null;
		if (!oldCandidate.getInfoCandidateSituation().equals(newCandidate.getInfoCandidateSituation())){
			candidateSituation = new CandidateSituation();
			
			// Change the Active Situation
			Iterator iterator = masterDegreeCandidate.getSituations().iterator();
			while(iterator.hasNext()) {
				ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
				if (candidateSituationTemp.getValidation().equals(new State(State.ACTIVE))){
					candidateSituation.setValidation(new State(State.INACTIVE));
					try {		
						sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituationTemp);
					} catch (ExcepcaoPersistencia ex) {
					  FenixServiceException newEx = new FenixServiceException("Persistence layer error");
					  newEx.fillInStackTrace();
					  throw newEx;
					}		
				}
			}
			
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
		}
		
		try {
            sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);
	    } catch (ExcepcaoPersistencia ex) {
	      FenixServiceException newEx = new FenixServiceException("Persistence layer error");
	      newEx.fillInStackTrace();
	      throw newEx;
	    }
	    
	    return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
    }
}