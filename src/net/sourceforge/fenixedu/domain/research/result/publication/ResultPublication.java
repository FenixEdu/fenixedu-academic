package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class ResultPublication extends ResultPublication_Base {

    public enum ScopeType {
	LOCAL, NATIONAL, INTERNATIONAL;
    }

    public ResultPublication() {
	super();
    }
    
    public Boolean hasResultPublicationRole() {
	if ((this instanceof Book) || (this instanceof BookPart) || (this instanceof Inproceedings))
	    return true;
	return false;
    }

    public List<ResultParticipation> getAuthors() {
	ArrayList<ResultParticipation> authors = new ArrayList<ResultParticipation>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getResultParticipationRole().equals(ResultParticipationRole.Author))
		authors.add(participation);
	}
	return authors;
    }

    public List<ResultParticipation> getEditors() {
	ArrayList<ResultParticipation> editors = new ArrayList<ResultParticipation>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getResultParticipationRole().equals(ResultParticipationRole.Editor))
		editors.add(participation);
	}
	return editors;
    }
    
    private void removeAssociations() {
	super.setPublisher(null);
	super.setOrganization(null);
    }

    @Override
    @Checked("ResultPredicates.writePredicate")
    public void delete() {
	removeAssociations();
	super.delete();
    }
    
    @Override
    public void removePublisher() {
	throw new DomainException("error.researcher.ResultPublication.call","removePublisher");
    }

    @Override
    public void removeOrganization() {
	throw new DomainException("error.researcher.ResultPublication.call","removeOrganization");
    }
    
    @Override
    public void setParticipationsOrder(List<ResultParticipation> newParticipationsOrder) {
	throw new DomainException("error.researcher.ResultPublication.call","setParticipationsOrder");
    }
}
