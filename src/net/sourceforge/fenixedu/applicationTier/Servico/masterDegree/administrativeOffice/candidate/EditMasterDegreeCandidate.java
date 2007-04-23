package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
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
	CandidateSituation newCandidateSituation = null;
	if (!oldCandidateSituation.getSituation().equals(
		newCandidate.getInfoCandidateSituation().getSituation())) {

	    oldCandidateSituation.setValidation(new State(State.INACTIVE));
	    newCandidateSituation = new CandidateSituation();
	    newCandidateSituation.setDateYearMonthDay(new YearMonthDay());
	    newCandidateSituation.setMasterDegreeCandidate(oldMasterDegreeCandidate);
	    newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
	    newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
	    newCandidateSituation.setValidation(new State(State.ACTIVE));

	} else if (oldCandidateSituation.getSituation().equals(
		newCandidate.getInfoCandidateSituation().getSituation())) {
	    newCandidateSituation = oldCandidateSituation;
	    newCandidateSituation.setDateYearMonthDay(oldCandidateSituation.getDateYearMonthDay());
	    newCandidateSituation.setMasterDegreeCandidate(oldMasterDegreeCandidate);
	    newCandidateSituation.setRemarks(newCandidate.getInfoCandidateSituation().getRemarks());
	    newCandidateSituation.setSituation(newCandidate.getInfoCandidateSituation().getSituation());
	}

	return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(oldMasterDegreeCandidate);
    }

}
