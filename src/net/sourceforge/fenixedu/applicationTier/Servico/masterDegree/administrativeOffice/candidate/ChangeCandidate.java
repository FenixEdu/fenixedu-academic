/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeCandidate implements IService {

    /**
     * The actor of this class.
     */
    public ChangeCandidate() {
    }

    public InfoMasterDegreeCandidate run(Integer oldCandidateID, InfoMasterDegreeCandidate newCandidate)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IPerson person = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        // IPerson candidatePerson = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate()
                    .readByOID(MasterDegreeCandidate.class, oldCandidateID, true);

            if (masterDegreeCandidate == null) {
                throw new ExcepcaoInexistente("Unknown Candidate !!");
            }

            person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                    masterDegreeCandidate.getPerson().getIdInternal(), true);

            person.setTipoDocumentoIdentificacao(newCandidate.getInfoPerson()
                    .getTipoDocumentoIdentificacao());
            person.setNumeroDocumentoIdentificacao(newCandidate.getInfoPerson()
                    .getNumeroDocumentoIdentificacao());

            // } catch (ExistingPersistentException ex) {
            // throw new ExistingServiceException(ex);
            // }

            // Get new Country
            ICountry nationality = null;
            if ((newCandidate.getInfoPerson().getInfoPais() != null) /*
                                                                         * &&
                                                                         * (person.getPais() !=
                                                                         * null)
                                                                         */
            ) {
                nationality = sp.getIPersistentCountry().readCountryByNationality(
                        newCandidate.getInfoPerson().getInfoPais().getNationality());
                person.setPais(nationality);
            }

            // Change personal Information
            person.setNascimento(newCandidate.getInfoPerson().getNascimento());
            person.setDataEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson()
                    .getDataEmissaoDocumentoIdentificacao());
            person.setDataValidadeDocumentoIdentificacao(newCandidate.getInfoPerson()
                    .getDataValidadeDocumentoIdentificacao());
            person.setLocalEmissaoDocumentoIdentificacao(newCandidate.getInfoPerson()
                    .getLocalEmissaoDocumentoIdentificacao());
            person.setNome(newCandidate.getInfoPerson().getNome());
            person.setGender(newCandidate.getInfoPerson().getSexo());
            person.setMaritalStatus(newCandidate.getInfoPerson().getMaritalStatus());
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

            if (!masterDegreeCandidate.getActiveCandidateSituation().getSituation().equals(
                    newCandidate.getInfoCandidateSituation().getSituation())) {

                // Change the Active Situation
                // Iterator iterator =
                // masterDegreeCandidate.getSituations().iterator();
                // while(iterator.hasNext()) {
                // ICandidateSituation candidateSituationTemp =
                // (ICandidateSituation) iterator.next();
                // if (candidateSituationTemp.getValidation().equals(new
                // State(State.ACTIVE))){
                //					
                // ICandidateSituation csTemp = new CandidateSituation();
                // csTemp.setIdInternal(candidateSituationTemp.getIdInternal());
                // ICandidateSituation candidateSituationFromBD =
                // (ICandidateSituation)
                // sp.getIPersistentCandidateSituation().readByOId(csTemp,
                // true);
                // candidateSituationFromBD.setValidation(new
                // State(State.INACTIVE));
                //					
                //					
                //					
                // }
                // }

                ICandidateSituation candidateSituationFromBD = (ICandidateSituation) sp
                        .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                                masterDegreeCandidate.getActiveCandidateSituation().getIdInternal(),
                                true);

                candidateSituationFromBD.setValidation(new State(State.INACTIVE));

                candidateSituation = new CandidateSituation();
                sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);

                Calendar calendar = Calendar.getInstance();
                candidateSituation.setDate(calendar.getTime());
                candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
                candidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());

                candidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
                candidateSituation.setValidation(new State(State.ACTIVE));

            }

            sp.confirmarTransaccao();
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            throw new ExistingServiceException(ex);
        }
        sp.iniciarTransaccao();

        IMasterDegreeCandidate masterDegreeCandidateFromBD = (IMasterDegreeCandidate) sp
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        masterDegreeCandidate.getIdInternal());

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidateFromBD);

        List situationsList = new ArrayList();
        Iterator iterator = masterDegreeCandidateFromBD.getSituations().iterator();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator
                            .next());
            situationsList.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }

        infoMasterDegreeCandidate.setSituationList(situationsList);

        return infoMasterDegreeCandidate;

    }
}