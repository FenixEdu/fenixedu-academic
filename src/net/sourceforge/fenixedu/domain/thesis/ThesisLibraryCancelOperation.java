package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * Library operation for canceling an archive or pending operation.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryCancelOperation extends ThesisLibraryCancelOperation_Base {
    public ThesisLibraryCancelOperation(Thesis thesis, Person performer) {
	super();
	if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis())
	    throw new DomainException("thesis.makepending.notEvaluatedNorPendingArchive");
	init(thesis, performer);
    }

    public ThesisLibraryCancelOperation(Integer thesisId, Integer performerId) {
	this(RootDomainObject.getInstance().readThesisByOID(thesisId), (Person) RootDomainObject.getInstance().readPartyByOID(
		performerId));
    }

    @Override
    public ThesisLibraryState getState() {
	return ThesisLibraryState.NOT_DEALT;
    }

    @Override
    public String getPendingComment() {
	return null;
    }

    @Override
    public String getLibraryReference() {
	return null;
    }
}
