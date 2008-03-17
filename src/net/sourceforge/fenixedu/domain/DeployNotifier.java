package net.sourceforge.fenixedu.domain;

import java.io.IOException;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Minutes;
import org.joda.time.Period;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

public class DeployNotifier extends DeployNotifier_Base {

    private static transient Boolean active = null;
    
    public DeployNotifier() {
	if (RootDomainObject.getInstance().getDeployNotifier() != null) {
	    throw new DomainException("There's already a deploy notifier");
	}
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Integer getEstimateMinutesForDeploy() {
	DateTime now = new DateTime();
	DateTime timeStamp = getTimeStamp();
	
	return now.isBefore(timeStamp) ?  new Period(now,timeStamp).get(DurationFieldType.minutes()) : Minutes.ZERO.getMinutes();
    }
    
    public Boolean getNotifierState() throws IOException {
	if(active == null) {
	    final Properties properties = PropertiesManager.loadProperties("/build.properties");
	    active = Boolean.valueOf(properties.getProperty("notifier.active"));
	}
	
	return active;
    }
}
