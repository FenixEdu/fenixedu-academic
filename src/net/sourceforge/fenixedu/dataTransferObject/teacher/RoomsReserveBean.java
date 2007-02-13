package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class RoomsReserveBean implements Serializable{

    private MultiLanguageString subject;
    
    private MultiLanguageString description;
    
    private DomainReference<Person> personReference;
    
    private DomainReference<PunctualRoomsOccupationRequest> requestReference;
    
    public RoomsReserveBean(Person requestor) {
	setRequestor(requestor);	
    }
    
    public RoomsReserveBean(Person requestor, PunctualRoomsOccupationRequest request) {
	setRequestor(requestor);
	setReserveRequest(request);
    }
    
    public Person getRequestor() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setRequestor(Person requestor) {
        this.personReference = (requestor != null) ? new DomainReference<Person>(requestor) : null;        
    }
    
    public PunctualRoomsOccupationRequest getReserveRequest() {
        return (this.requestReference != null) ? this.requestReference.getObject() : null;
    }

    public void setReserveRequest(PunctualRoomsOccupationRequest request) {
        this.requestReference = (request != null) ? new DomainReference<PunctualRoomsOccupationRequest>(request) : null;        
    }

    public MultiLanguageString getDescription() {
        return description;
    }

    public void setDescription(MultiLanguageString description) {
        this.description = description;
    }

    public MultiLanguageString getSubject() {
        return subject;
    }

    public void setSubject(MultiLanguageString subject) {
        this.subject = subject;
    }
}
