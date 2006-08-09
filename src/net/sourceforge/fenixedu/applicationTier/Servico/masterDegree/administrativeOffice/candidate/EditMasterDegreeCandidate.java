package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

public class EditMasterDegreeCandidate extends Service {

    public InfoMasterDegreeCandidate run(Integer oldCandidateID, InfoMasterDegreeCandidate newCandidate)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID( oldCandidateID);
        if (masterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Candidate !!");
        }

        // Change personal Information
        Person person = masterDegreeCandidate.getPerson();
        Country country = null;
        if ((newCandidate.getInfoPerson().getInfoPais() != null)) {
            country = Country.readCountryByNationality(newCandidate.getInfoPerson().getInfoPais().getNationality());
        }
        person.edit(newCandidate.getInfoPerson(), country);

        // Change Candidate Information
        masterDegreeCandidate.setAverage(newCandidate.getAverage());
        masterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
        masterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
        masterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
        masterDegreeCandidate.setSpecializationArea(newCandidate.getSpecializationArea());

        // Change Situation
        CandidateSituation oldCandidateSituation = masterDegreeCandidate.getActiveCandidateSituation();
        if (!oldCandidateSituation.getSituation().equals(
                newCandidate.getInfoCandidateSituation().getSituation())) {

            oldCandidateSituation.setValidation(new State(State.INACTIVE));

            CandidateSituation newCandidateSituation = new CandidateSituation();
            newCandidateSituation.setDate(Calendar.getInstance().getTime());
            newCandidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
            newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
            newCandidateSituation.setValidation(new State(State.ACTIVE));

            if (person.getEmail() != null) {
                sendEmailToCandidate(masterDegreeCandidate, newCandidateSituation);
            }
        }

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);

        return infoMasterDegreeCandidate;

    }

    private void sendEmailToCandidate(MasterDegreeCandidate masterDegreeCandidate,
            CandidateSituation candidateSituation) {
        ResourceBundle rb = ResourceBundle.getBundle("resources.ApplicationResources");
        List<String> toList = new ArrayList<String>();
        List CCList, BCCList;
        CCList = new ArrayList();
        BCCList = new ArrayList();
        toList.add(masterDegreeCandidate.getPerson().getEmail());
        String fromName, from, subject;
        fromName = rb.getString("masterDegreeCandidate.email.fromName");
        from = rb.getString("masterDegreeCandidate.email.fromEmail");
        subject = rb.getString("masterDegreeCandidate.email.subject")
                + masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getName();
        StringBuilder text = new StringBuilder();
        text.append(rb.getString("masterDegreeCandidate.email.greeting"));
        text.append(" ");
        text.append(masterDegreeCandidate.getPerson().getNome());
        text.append(rb.getString("masterDegreeCandidate.email.period"));
        text.append(rb.getString("masterDegreeCandidate.email.newLine"));
        text.append(rb.getString("masterDegreeCandidate.email.body"));
        text.append(candidateSituation.getSituation().toString());
        text.append(rb.getString("masterDegreeCandidate.email.period"));
        text.append(rb.getString("masterDegreeCandidate.email.newLine"));
        text.append(rb.getString("masterDegreeCandidate.email.newLine"));
        text.append(rb.getString("masterDegreeCandidate.email.goodbye"));
        try {
            SendMail sendMailService = new SendMail();
            sendMailService.run(toList, CCList, BCCList, fromName, from, subject, text.toString());
        } catch (Exception e) {
        }
    }

}
