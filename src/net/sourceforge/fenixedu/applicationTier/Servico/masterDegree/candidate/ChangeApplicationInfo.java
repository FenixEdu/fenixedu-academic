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
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePersonalContactInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class ChangeApplicationInfo extends Service {

    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate,
            InfoPerson infoPerson, IUserView userView, Boolean isNewPerson)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(
                        newMasterDegreeCandidate.getInfoExecutionDegree().getIdInternal());
        
        Person person = Person.readByDocumentIdNumberAndIdDocumentType(
                newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
        
         

        MasterDegreeCandidate existingMasterDegreeCandidate = person.getMasterDegreeCandidateByExecutionDegree(executionDegree);
        if (existingMasterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Candidate !!");
        }

        // Change Personal Information
        if (isNewPerson) {
            Country country = null;
            if ((infoPerson.getInfoPais() != null)) {
                country = Country.readCountryByNationality(infoPerson.getInfoPais().getNationality());
            }
            
            person = existingMasterDegreeCandidate.getPerson();
            person.edit(infoPerson, country);
            
        } else {
            Service service = new ChangePersonalContactInformation();
            ((ChangePersonalContactInformation) service).run(userView, infoPerson);
        }

        // Change Candidate Information
        existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
        existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
        existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate
                .getMajorDegreeSchool());
        existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
        existingMasterDegreeCandidate.setSpecializationArea(newMasterDegreeCandidate
                .getSpecializationArea());

        CandidateSituation oldCandidateSituation = existingMasterDegreeCandidate
                .getActiveCandidateSituation();
        oldCandidateSituation.setValidation(new State(State.INACTIVE));

        CandidateSituation activeCandidateSituation = new CandidateSituation();
        activeCandidateSituation.setDate(Calendar.getInstance().getTime());
        activeCandidateSituation.setSituation(SituationName.PENDENT_COM_DADOS_OBJ);
        activeCandidateSituation.setValidation(new State(State.ACTIVE));
        activeCandidateSituation.setMasterDegreeCandidate(existingMasterDegreeCandidate);
        existingMasterDegreeCandidate.getSituations().add(activeCandidateSituation);

        return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(existingMasterDegreeCandidate);

    }

}