package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import pt.ist.fenixWebFramework.services.Service;

public class ConfirmConvoke {

    @Service
    public static void run(OtherCourseVigilancy convoke) {
        convoke.setConfirmed(true);
    }

}