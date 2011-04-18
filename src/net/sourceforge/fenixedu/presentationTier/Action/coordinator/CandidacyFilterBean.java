package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.finalDegreeWork.CandidacyAttributionType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;

import org.apache.commons.collections.Predicate;

public class CandidacyFilterBean implements Serializable {
    
    private CandidacyAttributionType attributionStatus;

    public CandidacyFilterBean(CandidacyAttributionType status) {
	setStatus(status);
    }

    private void setStatus(CandidacyAttributionType status) {
	attributionStatus = status;
    }

    public CandidacyAttributionType getStatus() {
	return attributionStatus;
    }

    public Set<Predicate> getPredicates() {
	final Set<Predicate> predicates = new HashSet<Predicate>();
	predicates.add(new FinalDegreeWorkGroup.IsValidGroupPredicate());
	if (attributionStatus != null) {
	    predicates.add(new FinalDegreeWorkGroup.AttributionStatusPredicate(attributionStatus));
	}
	return predicates;
    }

    public void setFromRequest(HttpServletRequest request) {
	String filter = request.getParameter("filter");
	if (filter != null) {
	    try {
		CandidacyAttributionType status = CandidacyAttributionType.valueOf(filter);
		setStatus(status);
	    } catch (IllegalArgumentException iae) {
	    }
	}
    }

    @Override
    public String toString() {
	return attributionStatus.getDescription();
    }
}
