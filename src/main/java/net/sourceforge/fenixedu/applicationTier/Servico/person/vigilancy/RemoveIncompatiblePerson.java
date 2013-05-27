package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveIncompatiblePerson {

    @Service
    public static void run(VigilantWrapper vigilantWrapper) {
        vigilantWrapper.getPerson().getIncompatibleVigilantPerson().removeIncompatibleVigilantPerson();
        vigilantWrapper.getPerson().removeIncompatibleVigilantPerson();
    }

}