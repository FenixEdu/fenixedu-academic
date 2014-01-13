package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import pt.ist.fenixframework.Atomic;

public class RemoveIncompatiblePerson {

    @Atomic
    public static void run(VigilantWrapper vigilantWrapper) {
        vigilantWrapper.getPerson().getIncompatibleVigilantPerson().setIncompatibleVigilantPerson(null);
        vigilantWrapper.getPerson().setIncompatibleVigilantPerson(null);
    }

}