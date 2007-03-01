package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class AssiduousnessRecord extends AssiduousnessRecord_Base {

    public static final Comparator<AssiduousnessRecord> COMPARATORY_BY_DATE = new BeanComparator("date");

    public AssiduousnessRecord() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public DateTime getDateWithoutSeconds() {
	return getDate().withField(DateTimeFieldType.secondOfMinute(), 0);

    }

    public boolean isClocking() {
	return false;
    }

    public boolean isLeave() {
	return false;
    }
    
    public boolean isMissingClocking() {
	return false;
    }

    public boolean isAnulated() {
	return (getAnulation() != null && getAnulation().getState() == AnulationState.VALID);
    }

}
