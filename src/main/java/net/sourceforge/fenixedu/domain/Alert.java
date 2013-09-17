package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class Alert extends Alert_Base {

    public Alert() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRootDomainObjectForActiveAlerts(RootDomainObject.getInstance());
        setWhenCreated(new DateTime());
    }

    protected void init(final MultiLanguageString subject, final MultiLanguageString body) {
        checkParameters(subject, body);
        super.setSubject(subject);
        super.setBody(body);
        super.setActive(Boolean.TRUE);
    }

    private void checkParameters(MultiLanguageString subject, MultiLanguageString body) {
        String[] args = {};
        if (subject == null) {
            throw new DomainException("error.alert.Alert.subject.cannot.be.null", args);
        }
        String[] args1 = {};
        if (body == null) {
            throw new DomainException("error.alert.Alert.body.cannot.be.null", args1);
        }
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
        throw new DomainException("error.alert.Alert.cannot.modify.subject");
    }

    @Override
    public void setBody(MultiLanguageString body) {
        throw new DomainException("error.alert.Alert.cannot.modify.body");
    }

    @Override
    public void setActive(Boolean active) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.alert.Alert.cannot.modify.active");
    }

    @Override
    public MultiLanguageString getBody() {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.alert.Alert.use.getFormattedBody.instead");
    }

    @Override
    public MultiLanguageString getSubject() {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.alert.Alert.use.getFormattedSubject.instead");
    }

    public MultiLanguageString getFormattedBody() {
        return super.getBody();
    }

    public MultiLanguageString getFormattedSubject() {
        return super.getSubject();
    }

    public void fire() {
        if (isToDiscard()) {
            discard();
            return;
        }

        if (!isToFire()) {
            return;
        }

        generateMessage();
        super.setFireDate(new DateTime());

        // check again, because alert may be discarded after send messages
        if (isToDiscard()) {
            discard();
        }
    }

    public void discard() {
        super.setRootDomainObjectForActiveAlerts(null);
        super.setActive(false);
    }

    abstract public String getDescription();

    abstract protected boolean isToDiscard();

    abstract protected boolean isToFire();

    abstract protected void generateMessage();

    abstract public boolean isToSendMail();

    public boolean isCancelable() {
        return isToFire();
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasBody() {
        return getBody() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasRootDomainObjectForActiveAlerts() {
        return getRootDomainObjectForActiveAlerts() != null;
    }

    @Deprecated
    public boolean hasFireDate() {
        return getFireDate() != null;
    }

}
