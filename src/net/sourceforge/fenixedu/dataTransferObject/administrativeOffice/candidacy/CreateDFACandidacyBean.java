package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class CreateDFACandidacyBean implements Serializable{

    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    private DomainReference<ExecutionDegree> executionDegree;
    private String name;
    private String identificationNumber;
    private IDDocumentType idDocumentType;
    
    public Degree getDegree() {
        return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
        this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null; 
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject(); 
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan) : null; 
    }
    
    public ExecutionDegree getExecutionDegree() {
    	return (this.executionDegree == null) ? null : this.executionDegree.getObject();
    }
    
    public void setExecutionDegree(ExecutionDegree executionDegree) {
    	this.executionDegree = (executionDegree != null) ? new DomainReference<ExecutionDegree>(executionDegree) : null;
    }

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
