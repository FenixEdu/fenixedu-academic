package net.sourceforge.fenixedu.domain.phd.log;

import java.util.Locale;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class PhdLogEntry extends PhdLogEntry_Base {

    private PhdLogEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private PhdLogEntry(final String activityClassName, final String message, final PhdProgramProcess process) {
        setResponsible(AccessControl.getPerson());
        setWhenOccured(new DateTime());
        setActivityClassName(activityClassName);
        setPhdProgramProcess(process);
        setState(process.getActiveState().getLocalizedName(new Locale("pt", "PT")));
        setMessage(message);
    }

    public String getResponsibleName() {
        if (getResponsible() == null) {
            return "-";
        }

        return getResponsible().getName();
    }

    static PhdLogEntry createLog(final String activityClassName, final String message, final PhdProgramProcess process) {
        return new PhdLogEntry(activityClassName, message, process);
    }

}
