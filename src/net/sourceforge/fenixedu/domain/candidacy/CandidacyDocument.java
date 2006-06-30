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
    
    public String getUploadTime(){
        if(getFileUploadTime() != null){
            return getFileUploadTime().toString("dd/MM/yyyy hh:mm");
        }
        return "file.not.uploaded.yet";
    }
}
