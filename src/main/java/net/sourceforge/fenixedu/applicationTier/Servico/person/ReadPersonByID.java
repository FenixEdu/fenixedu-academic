package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadPersonByID {

    @Atomic
    public static InfoPerson run(String externalId) {
        return InfoPerson.newInfoFromDomain((Person) FenixFramework.getDomainObject(externalId));
    }
}