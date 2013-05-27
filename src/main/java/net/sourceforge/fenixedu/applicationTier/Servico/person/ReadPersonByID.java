package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPersonByID {

    @Service
    public static InfoPerson run(Integer idInternal) {
        return InfoPerson.newInfoFromDomain((Person) RootDomainObject.getInstance().readPartyByOID(idInternal));
    }
}