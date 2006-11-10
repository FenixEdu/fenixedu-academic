package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class DomainObjectActionLog extends DomainObjectActionLog_Base {

    public final static Comparator<DomainObjectActionLog> COMPARATOR_BY_INSTANT = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new ReverseComparator(new BeanComparator("instant")));
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("idInternal"));
    }
    
    public DomainObjectActionLog(Person person, DomainObject domainObject, String action,
	    Map<String, Object> parameters) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPerson(person);
	setPersonUsername(person.getUsername());	
	setKeyDomainObject(domainObject.getIdInternal());
	setDomainObjectClassName(domainObject.getClass().getName());
	setAction(action);
	setInstant(new DateTime());
	fillDomainObjectAtionLogEntries(parameters);
    }
       
    @Override
    public void setDomainObjectClassName(String domainObjectClassName) {
	if (domainObjectClassName == null || StringUtils.isEmpty(domainObjectClassName.trim())) {
	    throw new DomainException("error.domainObjectActionLog.empty.domainObjectClassName");
	}
	super.setDomainObjectClassName(domainObjectClassName);
    }

    @Override
    public void setKeyDomainObject(Integer keyDomainObject) {
	if (keyDomainObject == null) {
	    throw new DomainException("error.domainObjectActionLog.empty.keyDomainObject");
	}
	super.setKeyDomainObject(keyDomainObject);
    }

    @Override
    public void setPerson(Person person) {
	if (person == null) {
	    throw new DomainException("error.domainObjectActionLog.empty.person");
	}
	super.setPerson(person);
    }

    private void fillDomainObjectAtionLogEntries(Map<String, Object> parametersAndValues) {
	for (String parameter : parametersAndValues.keySet()) {
	    Object object = parametersAndValues.get(parameter);
	    new DomainObjectActionLogEntry(parameter, (object != null) ? object.toString() : null, this);
	}
    }

    @Override
    public String getPersonUsername() {
	if (getPerson() != null) {
	    getPerson().getUsername();
	}
	return super.getPersonUsername();
    }

    public String getChangedomainObject() {
	return getDomainObjectClassName() + "(" + getKeyDomainObject() + ")";
    }

    public String getPersonName() {
	return (getPerson() != null) ? getPerson().getName() : null;
    }
    
    public String getPresentationEntries() {
	StringBuilder stringBuilder = new StringBuilder();
	for (DomainObjectActionLogEntry entry : getDomainObjectActionLogEntriesSet()) {
	    stringBuilder.append(entry.getPresentationNameValue()).append(";");
	}
	return stringBuilder.toString();
    }
}
