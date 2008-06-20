package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class IngressionInformationBean implements Serializable {

    static private final List<RegistrationAgreement> AGREEMENTS_TO_REQUEST_INFORMATION = Arrays.asList(RegistrationAgreement.AFA,
	    RegistrationAgreement.MA);

    private RegistrationAgreement registrationAgreement;

    private String agreementInformation;

    private Ingression ingression;

    private EntryPhase entryPhase;

    private YearMonthDay studiesStartDate;

    private YearMonthDay homologationDate;

    private boolean requestAgreementInformation;

    public IngressionInformationBean() {
	super();
	this.registrationAgreement = RegistrationAgreement.NORMAL;
	requestAgreementInformation = false;
    }

    public RegistrationAgreement getRegistrationAgreement() {
	return registrationAgreement;
    }

    public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
	this.registrationAgreement = registrationAgreement;
	this.requestAgreementInformation = AGREEMENTS_TO_REQUEST_INFORMATION.contains(registrationAgreement);
    }

    public boolean hasRegistrationAgreement() {
	return getRegistrationAgreement() != null;
    }

    public String getAgreementInformation() {
	return agreementInformation;
    }

    public void setAgreementInformation(String agreementInformation) {
	this.agreementInformation = agreementInformation;
    }

    public boolean isRequestAgreementInformation() {
	return requestAgreementInformation;
    }

    public Ingression getIngression() {
	return ingression;
    }

    public void setIngression(Ingression ingression) {
	this.ingression = ingression;
    }

    public EntryPhase getEntryPhase() {
	return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
	this.entryPhase = entryPhase;
    }

    public void clearIngressionAndEntryPhase() {
	this.ingression = null;
	this.entryPhase = null;
	this.studiesStartDate = null;
	this.homologationDate = null;
    }

    public void clearAgreement() {
	this.registrationAgreement = RegistrationAgreement.NORMAL;
	this.agreementInformation = null;
    }

    public YearMonthDay getHomologationDate() {
	return homologationDate;
    }

    public void setHomologationDate(YearMonthDay homologationDate) {
	this.homologationDate = homologationDate;
    }

    public YearMonthDay getStudiesStartDate() {
	return studiesStartDate;
    }

    public void setStudiesStartDate(YearMonthDay studiesStartDate) {
	this.studiesStartDate = studiesStartDate;
    }

}
