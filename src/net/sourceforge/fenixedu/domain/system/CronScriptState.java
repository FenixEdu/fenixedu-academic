package net.sourceforge.fenixedu.domain.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.ByteArray;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public class CronScriptState extends CronScriptState_Base {

	public static final Comparator<CronScriptState> CRON_SCRIPT_STATE_COMPARATOR_BY_ABSOLUTE_EXECUTION_ORDER =
			new BeanComparator("absoluteExecutionOrder");

	public CronScriptState() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCronScriptClassname(getClass().getName());
        // TODO : set absoluteExecutionOrder
        setRegistrationDate(new DateTime());
        setIsCurrentlyRunning(Boolean.FALSE);
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
		final byte[] byteArray = getContext().getBytes();
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		final Serializable context = (Serializable) objectInputStream.readObject();
		objectInputStream.close();
		return context;
	}

}
