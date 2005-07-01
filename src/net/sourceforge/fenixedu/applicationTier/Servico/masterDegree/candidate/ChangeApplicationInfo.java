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

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePersonalInfo;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeApplicationInfo implements IService {

    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate,
            InfoPerson infoPerson, IUserView userView, Boolean isNewPerson)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionDegree executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree()
                .readByOID(ExecutionDegree.class,
                        newMasterDegreeCandidate.getInfoExecutionDegree().getIdInternal());

        IMasterDegreeCandidate existingMasterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
                        newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                        newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao(),
                        executionDegree.getIdInternal(), newMasterDegreeCandidate.getSpecialization());

        if (existingMasterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Candidate !!");
        }

        // Change Personal Information
        if (isNewPerson) {
            changeAllPersonInfo(sp, infoPerson, existingMasterDegreeCandidate.getPerson());
        } else {
            IService service = new ChangePersonalInfo();
            ((ChangePersonalInfo) service).run(userView, infoPerson);
        }

        // Change Candidate Information
        sp.getIPersistentMasterDegreeCandidate().simpleLockWrite(existingMasterDegreeCandidate);
        existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
        existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
        existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate
                .getMajorDegreeSchool());
        existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
        existingMasterDegreeCandidate.setSpecializationArea(newMasterDegreeCandidate
                .getSpecializationArea());

        ICandidateSituation oldCandidateSituation = existingMasterDegreeCandidate
                .getActiveCandidateSituation();
        oldCandidateSituation.setValidation(new State(State.INACTIVE));
        sp.getIPersistentCandidateSituation().simpleLockWrite(oldCandidateSituation);

        ICandidateSituation activeCandidateSituation = new CandidateSituation();
        sp.getIPersistentCandidateSituation().simpleLockWrite(activeCandidateSituation);
        activeCandidateSituation.setDate(Calendar.getInstance().getTime());
        activeCandidateSituation.setSituation(SituationName.PENDENT_COM_DADOS_OBJ);
        activeCandidateSituation.setValidation(new State(State.ACTIVE));
        activeCandidateSituation.setMasterDegreeCandidate(existingMasterDegreeCandidate);
        existingMasterDegreeCandidate.getSituations().add(activeCandidateSituation);

        return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(existingMasterDegreeCandidate);

    }

    private void changeAllPersonInfo(ISuportePersistente sp, InfoPerson infoPerson, IPerson person)
            throws ExcepcaoPersistencia {

        person.setIdDocumentType(infoPerson.getTipoDocumentoIdentificacao());
        person.setNumeroDocumentoIdentificacao(infoPerson.getNumeroDocumentoIdentificacao());
        if ((infoPerson.getInfoPais() != null)) {
            ICountry nationality = sp.getIPersistentCountry().readCountryByNationality(
                    infoPerson.getInfoPais().getNationality());
            person.setPais(nationality);
        }
        person.setNascimento(infoPerson.getNascimento());
        person.setDataEmissaoDocumentoIdentificacao(infoPerson.getDataEmissaoDocumentoIdentificacao());
        person.setDataValidadeDocumentoIdentificacao(infoPerson.getDataValidadeDocumentoIdentificacao());
        person.setLocalEmissaoDocumentoIdentificacao(infoPerson.getLocalEmissaoDocumentoIdentificacao());
        person.setNome(infoPerson.getNome());
        person.setGender(infoPerson.getSexo());
        person.setMaritalStatus(infoPerson.getMaritalStatus());
        person.setNomePai(infoPerson.getNomePai());
        person.setNomeMae(infoPerson.getNomeMae());
        person.setFreguesiaNaturalidade(infoPerson.getFreguesiaNaturalidade());
        person.setConcelhoNaturalidade(infoPerson.getConcelhoNaturalidade());
        person.setDistritoNaturalidade(infoPerson.getDistritoNaturalidade());
        person.setLocalidadeCodigoPostal(infoPerson.getLocalidadeCodigoPostal());
        person.setMorada(infoPerson.getMorada());
        person.setLocalidade(infoPerson.getLocalidade());
        person.setCodigoPostal(infoPerson.getCodigoPostal());
        person.setFreguesiaMorada(infoPerson.getFreguesiaMorada());
        person.setConcelhoMorada(infoPerson.getConcelhoMorada());
        person.setDistritoMorada(infoPerson.getDistritoMorada());
        person.setTelefone(infoPerson.getTelefone());
        person.setTelemovel(infoPerson.getTelemovel());
        person.setEmail(infoPerson.getEmail());
        person.setEnderecoWeb(infoPerson.getEnderecoWeb());
        person.setNumContribuinte(infoPerson.getNumContribuinte());
        person.setProfissao(infoPerson.getProfissao());
        person.setNacionalidade(infoPerson.getNacionalidade());
        sp.getIPessoaPersistente().simpleLockWrite(person);
    }

}