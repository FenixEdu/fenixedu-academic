package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public interface IAcademicServiceRequest {
    public String getDescription();
    public Language getLanguage();
    public Person getPerson();
    public boolean isRequestForRegistration();
    public boolean isRequestForPhd();
    public RegistryCode getRegistryCode();
    public boolean hasRegistryCode();
}
