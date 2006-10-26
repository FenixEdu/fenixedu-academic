package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class CreateDFACandidacyBean extends DFACandidacyBean implements Serializable {

    private String name;

    private String identificationNumber;

    private IDDocumentType idDocumentType;

    private String contributorNumber;
    
    private YearMonthDay candidacyDate = new YearMonthDay();

    public IDDocumentType getIdDocumentType() {
	return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType idDocumentType) {
	this.idDocumentType = idDocumentType;
    }

    public String getIdentificationNumber() {
	return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
	this.identificationNumber = identificationNumber;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getContributorNumber() {
	return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
	this.contributorNumber = contributorNumber;
    }

    public YearMonthDay getCandidacyDate() {
        return candidacyDate;
    }

    public void setCandidacyDate(YearMonthDay startDate) {
        this.candidacyDate = startDate;
    }
   
}
