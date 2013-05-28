package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * Library operation for canceling an archive or pending operation.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryCancelOperation extends ThesisLibraryCancelOperation_Base {
    public ThesisLibraryCancelOperation(Thesis thesis, Person performer) {
        super();
        if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis()) {
            throw new DomainException("thesis.makepending.notEvaluatedNorPendingArchive");
        }
        init(thesis, performer);
    }

    public ThesisLibraryCancelOperation(Integer thesisId, Integer performerId) {
        this(AbstractDomainObject.fromExternalId(thesisId), (Person) AbstractDomainObject.fromExternalId(
                performerId));
    }

    @Override
    public ThesisLibraryState getState() {
        return ThesisLibraryState.ARCHIVE_CANCELED;
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
