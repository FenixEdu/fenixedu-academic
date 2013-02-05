package net.sourceforge.fenixedu.domain.caseHandling;

import java.util.Comparator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class ProcessLog extends ProcessLog_Base {

    public static Comparator<ProcessLog> COMPARATOR_BY_WHEN = new Comparator<ProcessLog>() {
        @Override
        public int compare(ProcessLog leftProcessLog, ProcessLog rightProcessLog) {
            int comparationResult = leftProcessLog.getWhenDateTime().compareTo(rightProcessLog.getWhenDateTime());
            return (comparationResult == 0) ? leftProcessLog.getIdInternal().compareTo(rightProcessLog.getIdInternal()) : comparationResult;
        }
    };

    public ProcessLog(Process process, IUserView userView, Activity<?> activity) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setProcess(process);

        setUserName(userView != null ? userView.getUtilizador() : "PUBLICO");
        setActivity(activity.getClass().getName());
        setWhenDateTime(new DateTime());
    }

    public boolean isFor(final Class<? extends Activity> clazz) {
        return getActivity().equals(clazz.getName());
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
