package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CandidacyDocument extends CandidacyDocument_Base {

    public CandidacyDocument() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public CandidacyDocument(String description) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDocumentDescription(description);
    }

    @Deprecated
    public boolean hasDocumentDescription() {
        return getDocumentDescription() != null;
    }

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

}
