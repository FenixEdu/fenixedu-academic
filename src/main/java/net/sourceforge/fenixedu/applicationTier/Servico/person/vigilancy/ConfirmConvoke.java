package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import pt.ist.fenixframework.Atomic;

public class ConfirmConvoke {

    @Atomic
    public static void run(OtherCourseVigilancy convoke) {
        convoke.setConfirmed(true);
    }

}