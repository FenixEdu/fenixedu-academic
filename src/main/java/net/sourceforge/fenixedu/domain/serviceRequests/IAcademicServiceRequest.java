package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Person;

public interface IAcademicServiceRequest {
    public String getDescription();

    public Locale getLanguage();

    public Person getPerson();

    public boolean isRequestForRegistration();

    public boolean isRequestForPhd();

    public RegistryCode getRegistryCode();

    public boolean hasRegistryCode();
}
