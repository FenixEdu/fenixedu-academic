/*
 * ChangeMasterDegreeCandidate.java O Servico ChangeMasterDegreeCandidate altera
 * a informacao de um candidato de Mestrado Nota : E suposto os campos
 * (numeroCandidato, anoCandidatura, chaveCursoMestrado, username) nao se
 * puderem alterar Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorAplicacao.Servico.person;

import java.lang.reflect.InvocationTargetException;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import Dominio.ICountry;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ChangePersonalInfo implements IService
{

    /**
     * The actor of this class.
     */
    public ChangePersonalInfo()
    {
    }

    public UserView run(InfoPerson newInfoPerson, UserView userView) throws ExcepcaoInexistente,
            FenixServiceException, ExistingPersistentException, ExcepcaoPersistencia,
            IllegalAccessException, InvocationTargetException
    {

        ISuportePersistente sp = null;
        IPessoa person = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        if (person == null) { throw new ExcepcaoInexistente("Unknown Person !!"); }
        sp.getIPessoaPersistente().simpleLockWrite(person);

        // Get new Country
        ICountry nationality = null;
        if ((newInfoPerson.getInfoPais() == null)
                || (newInfoPerson.getInfoPais().getNationality().length() == 0))
        {
            person.setPais(null);
        }
        else
        {
            try
            {
                if ((person.getPais() == null)
                        || (!newInfoPerson.getInfoPais().getNationality().equals(
                                person.getPais().getNationality())))
                {
                    nationality = sp.getIPersistentCountry().readCountryByNationality(
                            newInfoPerson.getInfoPais().getNationality());
                    person.setPais(nationality);
                }
            }
            catch (ExcepcaoPersistencia ex)
            {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                newEx.fillInStackTrace();
                throw newEx;
            }
        }
        if (!newInfoPerson.getUsername().equals(userView.getUtilizador()))
        {
            userView.setUtilizador(newInfoPerson.getUsername());
            person.setUsername(newInfoPerson.getUsername());
        }

        // Change personal Information

        person.setNascimento(newInfoPerson.getNascimento());
        person
                .setDataEmissaoDocumentoIdentificacao(newInfoPerson
                        .getDataEmissaoDocumentoIdentificacao());
        person.setDataValidadeDocumentoIdentificacao(newInfoPerson
                .getDataValidadeDocumentoIdentificacao());
        person.setTipoDocumentoIdentificacao(newInfoPerson.getTipoDocumentoIdentificacao());
        person.setNumeroDocumentoIdentificacao(newInfoPerson.getNumeroDocumentoIdentificacao());
        person.setLocalEmissaoDocumentoIdentificacao(newInfoPerson
                .getLocalEmissaoDocumentoIdentificacao());
        person.setNome(newInfoPerson.getNome());

        person.setSexo(newInfoPerson.getSexo());
        person.setEstadoCivil(newInfoPerson.getEstadoCivil());

        person.setNomePai(newInfoPerson.getNomePai());
        person.setNomeMae(newInfoPerson.getNomeMae());
        person.setFreguesiaNaturalidade(newInfoPerson.getFreguesiaNaturalidade());
        person.setConcelhoNaturalidade(newInfoPerson.getConcelhoNaturalidade());
        person.setDistritoNaturalidade(newInfoPerson.getDistritoNaturalidade());
        person.setLocalidadeCodigoPostal(newInfoPerson.getLocalidadeCodigoPostal());
        person.setMorada(newInfoPerson.getMorada());
        person.setLocalidade(newInfoPerson.getLocalidade());
        person.setCodigoPostal(newInfoPerson.getCodigoPostal());
        person.setFreguesiaMorada(newInfoPerson.getFreguesiaMorada());
        person.setConcelhoMorada(newInfoPerson.getConcelhoMorada());
        person.setDistritoMorada(newInfoPerson.getDistritoMorada());
        person.setTelefone(newInfoPerson.getTelefone());
        person.setTelemovel(newInfoPerson.getTelemovel());
        person.setEmail(newInfoPerson.getEmail());
        person.setEnderecoWeb(newInfoPerson.getEnderecoWeb());
        person.setNumContribuinte(newInfoPerson.getNumContribuinte());
        person.setProfissao(newInfoPerson.getProfissao());
        person.setNacionalidade(newInfoPerson.getNacionalidade());

        //		//change the situation if the person is an candidate
        //		List masterDegreeCandidates = new ArrayList();
        //		try {
        //			masterDegreeCandidates =
        // sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidatesByUsername(person.getUsername());
        //			
        //	
        //		  	if (masterDegreeCandidates.size() != 0){
        //		  
        //			// Create a list with the active situations of the Candidate
        //			   ICandidateView candidateView = null;
        //			   Iterator iterator = masterDegreeCandidates.iterator();
        //			   List result = new ArrayList();
        //			   List situations = new ArrayList();
        //			   while(iterator.hasNext()){
        //				   IMasterDegreeCandidate masterDegreeCandidate =
        // (IMasterDegreeCandidate) iterator.next();
        //				   InfoMasterDegreeCandidate infoMasterDegreeCandidate =
        // Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
        //				   Iterator situationIterator =
        // masterDegreeCandidate.getSituations().iterator();
        //				  
        //				   while (situationIterator.hasNext()){
        //					   ICandidateSituation candidateSituation = (ICandidateSituation)
        // situationIterator.next();
        //					   
        //					   // Check if this is the Active Situation
        //					   if (candidateSituation.getValidation().equals(new
        // Util.State(Util.State.ACTIVE))){
        //							sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
        //							candidateSituation.setValidation(new
        // Util.State(Util.State.INACTIVE));
        //							
        //							//Create the New Candidate Situation
        //							candidateSituation = new CandidateSituation();
        //							Calendar calendar = Calendar.getInstance();
        //							candidateSituation.setDate(calendar.getTime());
        //							candidateSituation.setSituation(new
        // SituationName(SituationName.PENDENT_COM_DADOS));
        //							candidateSituation.setValidation(new Util.State(Util.State.ACTIVE));
        //							candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
        //							sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
        //							situations.add(Cloner.copyICandidateSituation2InfoCandidateSituation(candidateSituation));
        //							candidateView = new CandidateView(situations);
        //							userView.setCandidateView(candidateView);
        //					   }
        //				   }
        //			   }
        //			}
        //		} catch (ExcepcaoPersistencia ex) {
        //		   FenixServiceException newEx = new FenixServiceException("Persistence
        // layer error");
        //		   newEx.fillInStackTrace();
        //		   throw newEx;
        //	   }

        return userView;
    }
}