package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.Bennu;

public class DomainObjectActionLogEntry extends DomainObjectActionLogEntry_Base {

    public DomainObjectActionLogEntry(String name, String value, DomainObjectActionLog actionLog) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setName(name);
        setValue(value);
        setDomainObjectActionLog(actionLog);
    }

    @Override
    public void setName(String name) {
        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.domainObjectActionLogEntry.empty.name");
        }
        super.setName(name);
    }

    @Override
    public void setDomainObjectActionLog(DomainObjectActionLog domainObjectActionLog) {
        if (domainObjectActionLog == null) {
            throw new DomainException("error.domainObjectActionLogEntry.empty.domainObjectActionLog");
        }
        super.setDomainObjectActionLog(domainObjectActionLog);
    }

    public String getPresentationNameValue() {
        return getName() + "=" + getValue();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDomainObjectActionLog() {
        return getDomainObjectActionLog() != null;
    }

}
