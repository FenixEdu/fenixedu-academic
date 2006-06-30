package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class CreateDFACandidacyBean extends DFACandidacyBean implements Serializable{

    private String name;
    private String identificationNumber;
    private IDDocumentType idDocumentType;
    

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

}
