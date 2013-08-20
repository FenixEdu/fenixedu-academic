package net.sourceforge.fenixedu.domain.caseHandling;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import dml.DomainClass;

public abstract class Process extends Process_Base implements Comparable<Process> {

    private static Map<String, Activity<? extends Process>> startActivities;

    private static Integer lock = 0;

    private static void load() {
        startActivities = new HashMap<String, Activity<? extends Process>>();
        final Collection<DomainClass> domainClasses = FenixFramework.getDomainModel().getDomainClasses();

        for (final DomainClass domainClass : domainClasses) {
            try {
                final Class<?> clazz = Class.forName(domainClass.getFullName());
                if (Process.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    final Activity<? extends Process> activity = getStartActivity((Class<? extends Process>) clazz);
                    if (activity != null) {
                        startActivities.put(domainClass.getFullName(), activity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Activity<? extends Process> getStartActivity(Class<? extends Process> process) throws InstantiationException,
            IllegalAccessException {
        for (Class<?> clazz : process.getDeclaredClasses()) {
            if (Activity.class.isAssignableFrom(clazz)) {
                if (clazz.isAnnotationPresent(StartActivity.class)) {
                    return (Activity<? extends Process>) clazz.newInstance();
                }
            }
        }
        return null;
    }

    public static Activity<?> getStartActivity(String processName) {
        synchronized (lock) {
            if (startActivities == null) {
                load();
            }
        }

        Activity<? extends Process> activity = startActivities.get(processName);
        if (activity == null) {
            throw new RuntimeException("can't find process " + processName);
        }
        return activity;
    }

    public static <T extends Process> T createNewProcess(IUserView userView, Class<? extends Process> processClass, Object object) {
        try {
            return (T) getStartActivity(processClass).execute(null, userView, object);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Process> T createNewProcess(IUserView userView, String processName, Object object) {
        return (T) getStartActivity(processName).execute(null, userView, object);
    }

    abstract public List<Activity> getActivities();

    public Process() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public final Process executeActivity(IUserView userView, String activityId, Object object) {
        Activity activity = getActivity(activityId);

        return activity.execute(this, userView, object);
    }

    private Activity getActivity(String activityId) {
        for (Activity activity : getActivities()) {
            if (activity.getId().equals(activityId)) {
                return activity;
            }
        }
        return null;
    }

    public Activity getActivity(Class<? extends Activity> clazz) {
        return getActivity(clazz.getSimpleName());
    }

    @Override
    public int compareTo(Process process) {
        return getExternalId().compareTo(process.getExternalId());
    }

    public List<Activity> getAllowedActivities(final IUserView userView) {
        final List<Activity> result = new ArrayList<Activity>();
        for (final Activity activity : getActivities()) {
            try {
                activity.checkPreConditions(this, userView);
                result.add(activity);
            } catch (PreConditionNotValidException e) {
            }
        }
        return result;
    }

    public ProcessLog getLastProcessLog() {
        return Collections.max(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN);
    }

    public DateTime getWhenCreated() {
        return Collections.min(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getWhenDateTime();
    }

    public DateTime getWhenUpdated() {
        return Collections.max(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getWhenDateTime();
    }

    public String getCreatedBy() {
        return Collections.min(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getUserName();
    }

    public String getUpdatedBy() {
        return Collections.max(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getUserName();
    }

    protected DateTime getLastExecutionDateOf(final Class<? extends Activity> clazz) {
        final SortedSet<ProcessLog> logs = getSortedProcessLogs(clazz);
        return logs.isEmpty() ? null : logs.last().getWhenDateTime();
    }

    protected SortedSet<ProcessLog> getSortedProcessLogs(final Class<? extends Activity> clazz) {
        final SortedSet<ProcessLog> logs = new TreeSet<ProcessLog>(ProcessLog.COMPARATOR_BY_WHEN);
        for (final ProcessLog log : getProcessLogs()) {
            if (log.isFor(clazz)) {
                logs.add(log);
            }
        }
        return logs;
    }

    public abstract boolean canExecuteActivity(IUserView userView);

    public abstract String getDisplayName();
}
