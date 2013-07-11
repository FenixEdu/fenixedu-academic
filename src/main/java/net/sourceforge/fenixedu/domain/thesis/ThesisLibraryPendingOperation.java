package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.FenixFramework;

/**
 * Library operation of marking a thesis pending with an optional code
 * explaining why.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryPendingOperation extends ThesisLibraryPendingOperation_Base {
    public ThesisLibraryPendingOperation(Thesis thesis, Person performer, String comment) {
        super();
        if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis()) {
            throw new DomainException("thesis.makepending.notEvaluatedNorArchive");
        }
        init(thesis, performer);
        setPendingComment(comment);
    }

    public ThesisLibraryPendingOperation(String thesisId, String performerId, String comment) {
        this(FenixFramework.<Thesis> getDomainObject(thesisId), FenixFramework.<Person> getDomainObject(performerId),
                comment);
    }

    @Override
    public ThesisLibraryState getState() {
        return ThesisLibraryState.PENDING_ARCHIVE;
    }

    @Override
    public String getLibraryReference() {
        return null;
    }
    @Deprecated
    public boolean hasPendingComment() {
        return getPendingComment() != null;
    }

}
