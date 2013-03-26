package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class RoomsReserveBean implements Serializable {

    private MultiLanguageString subject;

    private MultiLanguageString description;

    private Person personReference;

    private PunctualRoomsOccupationRequest requestReference;

    public RoomsReserveBean(Person requestor) {
        setRequestor(requestor);
    }

    public RoomsReserveBean(Person requestor, PunctualRoomsOccupationRequest request) {
        setRequestor(requestor);
        setReserveRequest(request);
    }

    public Person getRequestor() {
        return this.personReference;
    }

    public void setRequestor(Person requestor) {
        this.personReference = requestor;
    }

    public PunctualRoomsOccupationRequest getReserveRequest() {
        return this.requestReference;
    }

    public void setReserveRequest(PunctualRoomsOccupationRequest request) {
        this.requestReference = request;
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
