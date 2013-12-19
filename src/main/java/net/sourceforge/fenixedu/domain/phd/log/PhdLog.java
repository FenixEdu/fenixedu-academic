package net.sourceforge.fenixedu.domain.phd.log;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

public class PhdLog {

    public static PhdLogEntry logActivity(final Activity activity, final PhdProgramProcess process, final User userView,
            final Object object) {

        ResourceBundle bundle = ResourceBundle.getBundle("resources/PhdResources", new Locale("pt", "PT"));

        String className = activity.getClass().getName();
        String message = bundle.getString("label." + className);

        return PhdLogEntry.createLog(className, message, process);
    }

}
