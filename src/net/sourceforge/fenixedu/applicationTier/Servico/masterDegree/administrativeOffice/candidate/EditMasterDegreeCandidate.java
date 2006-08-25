package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.State;

import org.joda.time.YearMonthDay;

public class EditMasterDegreeCandidate extends Service {

    public InfoMasterDegreeCandidate run(MasterDegreeCandidate oldMasterDegreeCandidate,
	    InfoMasterDegreeCandidate newCandidate, InfoPersonEditor infoPersonEditor)
	    throws ExcepcaoInexistente, FenixServiceException {

	if (oldMasterDegreeCandidate == null) {
	    throw new ExcepcaoInexistente("Unknown Candidate !!");
	}

	// Change personal Information
	Person person = oldMasterDegreeCandidate.getPerson();
	Country country = null;
	if ((infoPersonEditor.getInfoPais() != null)) {
	    country = Country.readCountryByNationality(infoPersonEditor.getInfoPais().getNationality());
	}
	person.edit(infoPersonEditor, country);

	// Change Candidate Information
	oldMasterDegreeCandidate.setAverage(newCandidate.getAverage());
	oldMasterDegreeCandidate.setMajorDegree(newCandidate.getMajorDegree());
	oldMasterDegreeCandidate.setMajorDegreeSchool(newCandidate.getMajorDegreeSchool());
	oldMasterDegreeCandidate.setMajorDegreeYear(newCandidate.getMajorDegreeYear());
	oldMasterDegreeCandidate.setSpecializationArea(newCandidate.getSpecializationArea());

	// Change Situation
	CandidateSituation oldCandidateSituation = oldMasterDegreeCandidate
		.getActiveCandidateSituation();
	if (!oldCandidateSituation.getSituation().equals(
		newCandidate.getInfoCandidateSituation().getSituation())) {

	    oldCandidateSituation.setValidation(new State(State.INACTIVE));

	    CandidateSituation newCandidateSituation = new CandidateSituation();
	    newCandidateSituation.setDateYearMonthDay(new YearMonthDay());
	    newCandidateSituation.setMasterDegreeCandidate(oldMasterDegreeCandidate);
	    newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
	    newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
	    newCandidateSituation.setValidation(new State(State.ACTIVE));

	    if (person.getEmail() != null) {
		sendEmailToCandidate(oldMasterDegreeCandidate, newCandidateSituation);
	    }
	}

	return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(oldMasterDegreeCandidate);
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
