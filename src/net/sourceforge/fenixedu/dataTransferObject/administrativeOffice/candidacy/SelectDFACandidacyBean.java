/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.SubstituteCandidacySituation;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectDFACandidacyBean implements Serializable {

    private DomainReference<DFACandidacy> candidacy;

    private CandidacySituationType selectionSituation;

    private String remarks;

    private Integer order;

    public SelectDFACandidacyBean(DFACandidacy candidacy) {
	super();
	if (candidacy != null) {
	    this.candidacy = new DomainReference<DFACandidacy>(candidacy);
	    if (candidacy.getActiveCandidacySituation().getCandidacySituationType().equals(
		    CandidacySituationType.SUBSTITUTE)) {
		this.selectionSituation = CandidacySituationType.SUBSTITUTE;
	    }
	}
    }

    public DFACandidacy getCandidacy() {
	return (this.candidacy == null) ? null : this.candidacy.getObject();
    }

    public void setCandidacy(DFACandidacy candidacy) {
	this.candidacy = (candidacy != null) ? new DomainReference<DFACandidacy>(candidacy) : null;
    }

    public Integer getOrder() {
	return order;
    }

    public void setOrder(Integer order) {
	this.order = order;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public CandidacySituationType getSelectionSituation() {
	return selectionSituation;
    }

    public void setSelectionSituation(CandidacySituationType selectionSituation) {
	this.selectionSituation = selectionSituation;
    }

}
