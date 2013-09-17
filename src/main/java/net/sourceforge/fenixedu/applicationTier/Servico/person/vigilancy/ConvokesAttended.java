package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import pt.ist.fenixframework.Atomic;

public class ConvokesAttended {

    @Atomic
    public static void run(Vigilancy convoke, Boolean bool) {
        convoke.setAttendedToConvoke(bool);
    }

}