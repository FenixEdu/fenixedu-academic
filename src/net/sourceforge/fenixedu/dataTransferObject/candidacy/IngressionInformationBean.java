/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class IngressionInformationBean implements Serializable {

    private Ingression ingression;

    private EntryPhase entryPhase;

    private RegistrationAgreement registrationAgreement;

    private YearMonthDay studiesStartDate;

    private YearMonthDay homologationDate;

    public IngressionInformationBean() {
	super();
	this.registrationAgreement = RegistrationAgreement.NORMAL;
    }

    public EntryPhase getEntryPhase() {
	return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
	this.entryPhase = entryPhase;
    }

    public Ingression getIngression() {
	return ingression;
    }

    public void setIngression(Ingression ingression) {
	this.ingression = ingression;
    }

    public RegistrationAgreement getRegistrationAgreement() {
	return registrationAgreement;
    }

    public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
	this.registrationAgreement = registrationAgreement;
    }

    public void clearIngressionAndEntryPhase() {
	this.ingression = null;
	this.entryPhase = null;
	this.studiesStartDate = null;
	this.homologationDate = null;
    }

    public void clearAgreement() {
	this.registrationAgreement = RegistrationAgreement.NORMAL;
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
