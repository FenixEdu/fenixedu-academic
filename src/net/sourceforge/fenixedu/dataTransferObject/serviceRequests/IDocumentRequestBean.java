package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.student.Registration;

public interface IDocumentRequestBean {
    public Registration getRegistration();
    public boolean hasRegistration();
}
