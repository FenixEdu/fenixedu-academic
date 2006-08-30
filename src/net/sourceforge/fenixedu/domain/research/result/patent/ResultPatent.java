package net.sourceforge.fenixedu.domain.research.result.patent;

import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

import org.joda.time.Partial;

/**
 * ResultPatent attributes:
 * 
 * String title; String note; String url; Country country; String patentNumber;
 * ResultPatentType patentType; ResultPatentStatus patentStatus; Partial
 * registrationDate; Partial approvalDate; String local;
 */

public class ResultPatent extends ResultPatent_Base {

    public enum ResultPatentType {
	LOCAL, NATIONAL, INTERNATIONAL;

	public String getName() {
	    return name();
	}
    }

    public enum ResultPatentStatus {
	/* type defined in CERIF */
	APPLIED, PUBLISHED, GRANTED, MAINTAINED;

	public String getName() {
	    return name();
	}
    }

    public ResultPatent() {
	super();
    }

    /**
     * Constructor to create a ResultPatent with the given parameters.
     */
    public ResultPatent(Person participation, String title, String note, ResultPatentType patentType,
	    ResultPatentStatus patentStatus, Partial registrationDate, Partial approvalDate,
	    Country country, String local, String patentNumber, String url) {
	this();
	checkRequiredParameters(title, registrationDate, approvalDate);
	super.setParticipation(participation, ResultParticipationRole.Author);
	fillAllAttributes(title, note, patentType, patentStatus, registrationDate, approvalDate,
		country, local, patentNumber, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String note, ResultPatentType patentType,
	    ResultPatentStatus patentStatus, Partial registrationDate, Partial approvalDate,
	    Country country, String local, String patentNumber, String url) {
	checkRequiredParameters(title, registrationDate, approvalDate);
	fillAllAttributes(title, note, patentType, patentStatus, registrationDate, approvalDate,
		country, local, patentNumber, url);
	super.setModifyedByAndDate();
    }
    
    public Person getParticipation(){
	return null;
    }

    private void fillAllAttributes(String title, String note, ResultPatentType patentType,
	    ResultPatentStatus patentStatus, Partial registrationDate, Partial approvalDate,
	    Country country, String local, String patentNumber, String url) {
	super.setTitle(title);
	super.setNote(note);
	super.setPatentType(patentType);
	super.setPatentStatus(patentStatus);
	super.setRegistrationDate(registrationDate);
	super.setApprovalDate(approvalDate);
	super.setCountry(country);
	super.setLocal(local);
	super.setPatentNumber(patentNumber);
	super.setUrl(url);
    }
    
    private boolean isApprovalDateAfterRegistrationDate(Partial approvalDate, Partial registrationDate) {
	if (approvalDate.isEqual(registrationDate)) {
	    return true;
	}
	if (approvalDate.isAfter(registrationDate)) {
	    return true;
	}
	return false;
    }

    private void checkRequiredParameters(String title, Partial registrationDate, Partial approvalDate) {
	if (title == null || title.equals("")) {
	    throw new DomainException("error.researcher.ResultPatent.title.null");
	}
	if (registrationDate == null) {
	    throw new DomainException("error.researcher.ResultPatent.registrationDate.null");
	}
	if (approvalDate == null) {
	    throw new DomainException("error.researcher.ResultPatent.approvalDate.null");
	}
	if (!isApprovalDateAfterRegistrationDate(approvalDate, registrationDate)) {
	    throw new DomainException("error.researcher.ResultPatent.approval.before.registration");
	}
    }

    /**
     * Block individual setters
     */
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setTile");
    }

    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setNote");
    }

    @Override
    public void setPatentType(ResultPatentType patentType) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setPatentType");
    }

    @Override
    public void setPatentStatus(ResultPatentStatus patentStatus) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setPatentStatus");
    }

    @Override
    public void setRegistrationDate(Partial registrationDate) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setRegistrationDate");
    }

    @Override
    public void setApprovalDate(Partial approvalDate) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setApprovalDate");
    }

    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setCountry");
    }

    @Override
    public void setLocal(String local) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setLocal");
    }

    @Override
    public void setPatentNumber(String PatentNumber) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setPatentNumber");
    }

    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setUrl");
    }

    @Override
    protected void setModifyedByAndDate() {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setModifyedByAndDate");
    }

    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.ResultPatent.illegal.call","setParticipation");
    }
}
