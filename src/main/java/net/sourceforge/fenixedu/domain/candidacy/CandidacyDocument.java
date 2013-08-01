package net.sourceforge.fenixedu.domain.candidacy;

import pt.ist.bennu.core.domain.Bennu;

public class CandidacyDocument extends CandidacyDocument_Base {

    public CandidacyDocument() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CandidacyDocument(String description) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

}
