package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CandidateSituation extends CandidateSituation_Base {

    public CandidateSituation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CandidateSituation(Date date, String remarks, State validation, MasterDegreeCandidate masterDegreeCandidate,
	    SituationName situation) {

	this();
	setMasterDegreeCandidate(masterDegreeCandidate);
	setSituation(situation);
	setDate(date);
	setRemarks(remarks);
	setValidation(validation);
    }

    public void delete() {
	removeMasterDegreeCandidate();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
