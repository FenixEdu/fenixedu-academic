package net.sourceforge.fenixedu.domain.system;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Period;

public class CronScriptInvocation extends CronScriptInvocation_Base {

	public static final Comparator<CronScriptInvocation> COMPARATOR_BY_START_TIME = new BeanComparator("startTime");

	public CronScriptInvocation(final CronScriptState cronScriptState, final String serverID,
			final DateTime startTime, final DateTime endTime, final Boolean successful,
			final String log) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setCronScriptState(cronScriptState);
		setServerID(serverID);
		setStartTime(startTime);
		setEndTime(endTime);
		setSuccessful(successful);
		setLog(log);
	}

	public boolean hasReachedNextInvocationTime(final Period invocationPeriod) {
		return !getStartTime().plus(invocationPeriod).isAfterNow();
	}

}
