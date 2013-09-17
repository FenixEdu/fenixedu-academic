package net.sourceforge.fenixedu.domain.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.ByteArray;

import org.joda.time.DateTime;
import org.joda.time.Period;

import pt.utl.ist.fenix.tools.util.CollectionUtils;

public class CronScriptState extends CronScriptState_Base {

    public static final Comparator<CronScriptState> COMPARATOR_BY_ABSOLUTE_EXECUTION_ORDER = new Comparator<CronScriptState>() {

        @Override
        public int compare(CronScriptState o1, CronScriptState o2) {
            return o1.getAbsoluteExecutionOrder().compareTo(o2.getAbsoluteExecutionOrder());
        }

    };

    public static final Comparator<CronScriptState> COMPARATOR_BY_CRON_SCRIPT_CLASSNAME = new Comparator<CronScriptState>() {

        @Override
        public int compare(CronScriptState o1, CronScriptState o2) {
            return o1.getCronScriptClassname().compareTo(o2.getCronScriptClassname());
        }

    };

    private transient Class cronScriptClass = null;

    public static class RunNowExecutor implements FactoryExecutor {

        private final CronScriptState cronScriptState;

        public RunNowExecutor(final CronScriptState cronScriptState) {
            this.cronScriptState = cronScriptState;
        }

        @Override
        public Object execute() {
            cronScriptState.setRunNow(Boolean.TRUE);
            return null;
        }

    }

    public CronScriptState(final Class cronScriptClass, final Period invocationPeriod, final WhenToSendEmail whenToSendEmail,
            final String emails) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCronScriptClassname(cronScriptClass.getName());
        this.cronScriptClass = cronScriptClass;
        // TODO : set absoluteExecutionOrder
        setRegistrationDate(new DateTime());
        setActive(Boolean.TRUE);
        setIsCurrentlyRunning(Boolean.FALSE);
        setInvocationPeriod(invocationPeriod);
        setRunNow(Boolean.FALSE);
    }

    public void serializeContext(final Serializable context) {
        ByteArray byteArray;
        if (context != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(context);
                objectOutputStream.close();
                byteArray = new ByteArray(byteArrayOutputStream.toByteArray());
            } catch (final Exception exception) {
                byteArray = null;
            }
        } else {
            byteArray = null;
        }
        setContext(byteArray);
    }

    public Serializable deserializeContext() throws IOException, ClassNotFoundException {
        if (getContext() == null) {
            return null;
        }
        final byte[] byteArray = getContext().getBytes();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final Serializable context = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        return context;
    }

    public Class getCronScriptClass() {
        if (cronScriptClass == null) {
            try {
                cronScriptClass = Class.forName(getCronScriptClassname());
            } catch (ClassNotFoundException e) {
                cronScriptClass = null;
            }
        }
        return cronScriptClass;
    }

    public CronScriptInvocation getLastCronScriptInvocation() {
        final Set<CronScriptInvocation> invocationSet = getCronScriptInvocationsSet();
        return invocationSet.isEmpty() ? null : Collections.max(invocationSet, CronScriptInvocation.COMPARATOR_BY_START_TIME);
    }

    public boolean shouldBeRunNow() {
        if (getActive().booleanValue()) {
            final Period invocationPeriod = getInvocationPeriod();
            final CronScriptInvocation lastCronScriptInvocation = getLastCronScriptInvocation();
            final Boolean runNow = getRunNow();
            return lastCronScriptInvocation == null || (runNow != null && runNow.booleanValue())
                    || lastCronScriptInvocation.hasReachedNextInvocationTime(invocationPeriod);
        }
        return false;
    }

    public String getInvocationPeriodString() {
        if (getInvocationPeriod().getMinutes() > 0) {
            return "every " + getInvocationPeriod().getMinutes() + " minutes";
        }
        if (getInvocationPeriod().getHours() > 0) {
            return "every " + getInvocationPeriod().getHours() + " hours";
        }
        if (getInvocationPeriod().getDays() > 0) {
            return "every " + getInvocationPeriod().getDays() + " days";
        }
        if (getInvocationPeriod().getWeeks() > 0) {
            return "every " + getInvocationPeriod().getWeeks() + " weeks";
        }
        if (getInvocationPeriod().getMonths() > 0) {
            return "every " + getInvocationPeriod().getMonths() + " months";
        }
        if (getInvocationPeriod().getYears() > 0) {
            return "every " + getInvocationPeriod().getYears() + " years";
        }
        throw new DomainException("error.unknown.period.type");
    }

    public DateTime getLastInvocationStartTime() {
        final CronScriptInvocation lastCronScriptInvocation = getLastCronScriptInvocation();
        return lastCronScriptInvocation == null ? null : lastCronScriptInvocation.getStartTime();
    }

    public DateTime getLastInvocationEndTime() {
        final CronScriptInvocation lastCronScriptInvocation = getLastCronScriptInvocation();
        return lastCronScriptInvocation == null ? null : lastCronScriptInvocation.getEndTime();
    }

    public Boolean getSuccessful() {
        final CronScriptInvocation lastCronScriptInvocation = getLastCronScriptInvocation();
        return lastCronScriptInvocation == null ? null : lastCronScriptInvocation.getSuccessful();
    }

    public DateTime getNextExpectedInvocationTime() {
        final CronScriptInvocation lastCronScriptInvocation = getLastCronScriptInvocation();
        return lastCronScriptInvocation == null ? null : lastCronScriptInvocation.getStartTime().plus(getInvocationPeriod());
    }

    public SortedSet<CronScriptInvocation> getCronScriptInvocationsSetSortedByInvocationStartTime() {
        return CollectionUtils.constructSortedSet(getCronScriptInvocationsSet(), CronScriptInvocation.COMPARATOR_BY_START_TIME);
    }

    public void delete() {
        setRootDomainObject(null);
        for (final CronScriptInvocation cronScriptInvocation : getCronScriptInvocations()) {
            cronScriptInvocation.delete();
        }
        deleteDomainObject();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.system.CronScriptInvocation> getCronScriptInvocations() {
        return getCronScriptInvocationsSet();
    }

    @Deprecated
    public boolean hasAnyCronScriptInvocations() {
        return !getCronScriptInvocationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasWhenToSendEmail() {
        return getWhenToSendEmail() != null;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasEmails() {
        return getEmails() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIsCurrentlyRunning() {
        return getIsCurrentlyRunning() != null;
    }

    @Deprecated
    public boolean hasContext() {
        return getContext() != null;
    }

    @Deprecated
    public boolean hasRegistrationDate() {
        return getRegistrationDate() != null;
    }

    @Deprecated
    public boolean hasAbsoluteExecutionOrder() {
        return getAbsoluteExecutionOrder() != null;
    }

    @Deprecated
    public boolean hasRunNow() {
        return getRunNow() != null;
    }

    @Deprecated
    public boolean hasCronScriptClassname() {
        return getCronScriptClassname() != null;
    }

    @Deprecated
    public boolean hasInvocationPeriod() {
        return getInvocationPeriod() != null;
    }

}
