package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class AssiduousnessRecord extends AssiduousnessRecord_Base {

    static final public Comparator<AssiduousnessRecord> COMPARATOR_BY_DATE = new Comparator<AssiduousnessRecord>() {
	public int compare(final AssiduousnessRecord o1, final AssiduousnessRecord o2) {
	    return o1.getDate().compareTo(o2.getDate());
	}
    };

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
