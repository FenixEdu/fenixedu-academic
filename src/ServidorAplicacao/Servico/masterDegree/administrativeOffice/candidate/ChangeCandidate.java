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
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICountry;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.State;

public class ChangeCandidate implements IServico
{

    private static ChangeCandidate servico = new ChangeCandidate();

    /**
	 * The singleton access method of this class.
	 */
    public static ChangeCandidate getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ChangeCandidate()
    {
    }

    /**
	 * Returns the service name
	 */

    public final String getNome()
    {
        return "ChangeCandidate";
    }

    public InfoMasterDegreeCandidate run(Integer oldCandidateID, InfoMasterDegreeCandidate newCandidate)
        throws
            ExcepcaoInexistente,
            FenixServiceException,
            IllegalAccessException,
            InvocationTargetException,
            ExcepcaoPersistencia
    {

        ISuportePersistente sp = null;
        IPessoa person = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        //		IPessoa candidatePerson = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            IMasterDegreeCandidate masterDegreeCandidateTemp = new MasterDegreeCandidate();
            masterDegreeCandidateTemp.setIdInternal(oldCandidateID);

            masterDegreeCandidate =
                (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(
                    masterDegreeCandidateTemp,
                    true);

            if (masterDegreeCandidate == null)
            {
                throw new ExcepcaoInexistente("Unknown Candidate !!");
            }

            IPessoa personTemp = new Pessoa();
            personTemp.setIdInternal(masterDegreeCandidate.getPerson().getIdInternal());
            person = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true);

            person.setTipoDocumentoIdentificacao(
                newCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
            person.setNumeroDocumentoIdentificacao(
                newCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());

        } catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        }

        // Get new Country
        ICountry nationality = null;
        if ((newCandidate.getInfoPerson().getInfoPais() != null) /* && (person.getPais() != null) */
            )
        {
            nationality =
                sp.getIPersistentCountry().readCountryByNationality(
                    newCandidate.getInfoPerson().getInfoPais().getNationality());
            person.setPais(nationality);
        }

        // Change personal Information
        person.setNascimento(newCandidate.getInfoPerson().getNascimento());
        person.setDataEmissaoDocumentoIdentificacao(
            newCandidate.getInfoPerson().getDataEmissaoDocumentoIdentificacao());
        person.setDataValidadeDocumentoIdentificacao(
            newCandidate.getInfoPerson().getDataValidadeDocumentoIdentificacao());
        person.setLocalEmissaoDocumentoIdentificacao(
            newCandidate.getInfoPerson().getLocalEmissaoDocumentoIdentificacao());
        person.setNome(newCandidate.getInfoPerson().getNome());
        person.setSexo(newCandidate.getInfoPerson().getSexo());
        person.setEstadoCivil(newCandidate.getInfoPerson().getEstadoCivil());
        person.setNomePai(newCandidate.getInfoPerson().getNomePai());
        person.setNomeMae(newCandidate.getInfoPerson().getNomeMae());
        person.setFreguesiaNaturalidade(newCandidate.getInfoPerson().getFreguesiaNaturalidade());
        person.setConcelhoNaturalidade(newCandidate.getInfoPerson().getConcelhoNaturalidade());
        person.setDistritoNaturalidade(newCandidate.getInfoPerson().getDistritoNaturalidade());
        person.setLocalidadeCodigoPostal(newCandidate.getInfoPerson().getLocalidadeCodigoPostal());
        person.setMorada(newCandidate.getInfoPerson().getMorada());
        person.setLocalidade(newCandidate.getInfoPerson().getLocalidade());
        person.setCodigoPostal(newCandidate.getInfoPerson().getCodigoPostal());
        person.setFreguesiaMorada(newCandidate.getInfoPerson().getFreguesiaMorada());
        person.setConcelhoMorada(newCandidate.getInfoPerson().getConcelhoMorada());
        person.setDistritoMorada(newCandidate.getInfoPerson().getDistritoMorada());
        person.setTelefone(newCandidate.getInfoPerson().getTelefone());
        person.setTelemovel(newCandidate.getInfoPerson().getTelemovel());
        person.setEmail(newCandidate.getInfoPerson().getEmail());
        person.setEnderecoWeb(newCandidate.getInfoPerson().getEnderecoWeb());
        person.setNumContribuinte(newCandidate.getInfoPerson().getNumContribuinte());
        person.setProfissao(newCandidate.getInfoPerson().getProfissao());
        person.setNacionalidade(newCandidate.getInfoPerson().getNacionalidade());

        // Change Application Information
        masterDegreeCandidate.setAverage(newCandidate.getAverage());
        masterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
        masterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
        masterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
        masterDegreeCandidate.setSpecializationArea(newCandidate.getSpecializationArea());

        masterDegreeCandidate.setPerson(person);

        // Change Situation

        ICandidateSituation candidateSituation = null;

        if (!masterDegreeCandidate
            .getActiveCandidateSituation()
            .getSituation()
            .equals(newCandidate.getInfoCandidateSituation().getSituation()))
        {

            // Change the Active Situation
            //			Iterator iterator = masterDegreeCandidate.getSituations().iterator();
            //			while(iterator.hasNext()) {
            //				ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
            //				if (candidateSituationTemp.getValidation().equals(new State(State.ACTIVE))){
            //					
            //					ICandidateSituation csTemp = new CandidateSituation();
            //					csTemp.setIdInternal(candidateSituationTemp.getIdInternal());
            //					ICandidateSituation candidateSituationFromBD = (ICandidateSituation)
			// sp.getIPersistentCandidateSituation().readByOId(csTemp, true);
            //					candidateSituationFromBD.setValidation(new State(State.INACTIVE));
            //					
            //					
            //					
            //				}
            //			}

            ICandidateSituation candidateSituationTemp = new CandidateSituation();
            candidateSituationTemp.setIdInternal(
                masterDegreeCandidate.getActiveCandidateSituation().getIdInternal());
            ICandidateSituation candidateSituationFromBD =
                (ICandidateSituation) sp.getIPersistentCandidateSituation().readByOId(
                    candidateSituationTemp,
                    true);

            candidateSituationFromBD.setValidation(new State(State.INACTIVE));

            candidateSituation = new CandidateSituation();
            sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);

            Calendar calendar = Calendar.getInstance();
            candidateSituation.setDate(calendar.getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());

            candidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
            candidateSituation.setValidation(new State(State.ACTIVE));

        }

        sp.confirmarTransaccao();
        sp.iniciarTransaccao();

        IMasterDegreeCandidate masterDegreeCandidateTemp = new MasterDegreeCandidate();
        masterDegreeCandidateTemp.setIdInternal(masterDegreeCandidate.getIdInternal());

        IMasterDegreeCandidate masterDegreeCandidateFromBD =
            (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(
                masterDegreeCandidateTemp,
                false);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate =
            Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidateFromBD);

        List situationsList = new ArrayList();
        Iterator iterator = masterDegreeCandidateFromBD.getSituations().iterator();
        while (iterator.hasNext())
        {
            InfoCandidateSituation infoCandidateSituation =
                Cloner.copyICandidateSituation2InfoCandidateSituation(
                    (ICandidateSituation) iterator.next());
            situationsList.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }

        infoMasterDegreeCandidate.setSituationList(situationsList);

        return infoMasterDegreeCandidate;

    }
}