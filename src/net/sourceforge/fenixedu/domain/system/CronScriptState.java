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

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.ByteArray;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Period;

public class CronScriptState extends CronScriptState_Base {

    public static final Comparator<CronScriptState> COMPARATOR_BY_ABSOLUTE_EXECUTION_ORDER =
		new BeanComparator("absoluteExecutionOrder");

    private transient Class cronScriptClass = null;

    public CronScriptState(final Class cronScriptClass, final Period invocationPeriod) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCronScriptClassname(cronScriptClass.getName());
	this.cronScriptClass = cronScriptClass;
	// TODO : set absoluteExecutionOrder
	setRegistrationDate(new DateTime());
	setActive(Boolean.TRUE);
	setIsCurrentlyRunning(Boolean.FALSE);
	setInvocationPeriod(invocationPeriod);
    }

    public void serializeContext(final Serializable context) {
	ByteArray byteArray;
	if (context != null) {
	    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    try {
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(
			byteArrayOutputStream);
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

    public Class getCronScriptClass() throws ClassNotFoundException {
	if (cronScriptClass == null) {
	    cronScriptClass = Class.forName(getCronScriptClassname());
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
	    return lastCronScriptInvocation == null || lastCronScriptInvocation.hasReachedNextInvocationTime(invocationPeriod);
	}
	return false;
    }

}
