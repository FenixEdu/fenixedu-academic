package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.FenixFramework;

/**
 * Library operation of marking a thesis as archived (validated and with library
 * code set).
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryArchiveOperation extends ThesisLibraryArchiveOperation_Base {
    public ThesisLibraryArchiveOperation(Thesis thesis, Person performer, String libraryReference) {
        super();
        if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis()) {
            throw new DomainException("thesis.makepending.notEvaluatedNorPendingArchive");
        }
        init(thesis, performer);
        setLibraryReference(libraryReference);
    }

    public ThesisLibraryArchiveOperation(String thesisId, String performerId, String comment) {
        this(FenixFramework.<Thesis> getDomainObject(thesisId), FenixFramework.<Person> getDomainObject(performerId), comment);
    }

    @Override
    public ThesisLibraryState getState() {
        return ThesisLibraryState.ARCHIVE;
    }

    @Override
    public String getPendingComment() {
        return null;
    }

    @Deprecated
    public boolean hasLibraryReference() {
        return getLibraryReference() != null;
    }

}
