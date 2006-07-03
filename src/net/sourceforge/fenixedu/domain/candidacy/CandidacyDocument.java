package net.sourceforge.fenixedu.domain.candidacy;

import org.joda.time.DateTime;

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

    @Override
    public void setFile(CandidacyDocumentFile file) {
        setFileUploadTime(new DateTime());
        super.setFile(file);
    }

}
