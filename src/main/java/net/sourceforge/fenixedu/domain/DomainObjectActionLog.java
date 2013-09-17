package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class DomainObjectActionLog extends DomainObjectActionLog_Base {

    public final static Comparator<DomainObjectActionLog> COMPARATOR_BY_INSTANT = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new ReverseComparator(new BeanComparator("instant")));
        ((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public DomainObjectActionLog(Person person, DomainObject domainObject, String action, Map<String, Object> parameters) {

        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPerson(person);
        setPersonUsername(person.getUsername());
        setDomainObjectClassName(domainObject.getClass().getName());
        setDomainObjectExternalId(domainObject.getExternalId());
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
        throw new UnsupportedOperationException("Logs can no longer be created using Id Internals.");
    }

    @Override
    public void setDomainObjectExternalId(String domainObjectExternalId) {
        if (domainObjectExternalId == null || StringUtils.isEmpty(domainObjectExternalId.trim())) {
            throw new DomainException("error.domainObjectActionLog.empty.externalId");
        }
        super.setDomainObjectExternalId(domainObjectExternalId);
    }

    @Override
    public String getDomainObjectExternalId() {
        if (super.getDomainObjectExternalId() == null) {
            return "IdInternal: " + getKeyDomainObject();
        }
        return super.getDomainObjectExternalId();
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

    public String getChangedDomainObject() {
        return getDomainObjectClassName() + "(" + getDomainObjectExternalId() + ")";
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

    public static Set<DomainObjectActionLog> readDomainObjectActionLogsOrderedByInstant(
            Set<Class<? extends DomainObject>> domainObjectClasss) {

        Set<DomainObjectActionLog> resultList = new TreeSet<DomainObjectActionLog>(DomainObjectActionLog.COMPARATOR_BY_INSTANT);
        for (DomainObjectActionLog log : RootDomainObject.getInstance().getDomainObjectActionLogsSet()) {
            try {
                Class<?> domainObjectClass = Class.forName(log.getDomainObjectClassName());
                if (domainObjectClasss.contains(domainObjectClass)) {
                    resultList.add(log);
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return resultList;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DomainObjectActionLogEntry> getDomainObjectActionLogEntries() {
        return getDomainObjectActionLogEntriesSet();
    }

    @Deprecated
    public boolean hasAnyDomainObjectActionLogEntries() {
        return !getDomainObjectActionLogEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInstant() {
        return getInstant() != null;
    }

    @Deprecated
    public boolean hasAction() {
        return getAction() != null;
    }

    @Deprecated
    public boolean hasKeyDomainObject() {
        return getKeyDomainObject() != null;
    }

    @Deprecated
    public boolean hasDomainObjectExternalId() {
        return getDomainObjectExternalId() != null;
    }

    @Deprecated
    public boolean hasPersonUsername() {
        return getPersonUsername() != null;
    }

    @Deprecated
    public boolean hasDomainObjectClassName() {
        return getDomainObjectClassName() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
