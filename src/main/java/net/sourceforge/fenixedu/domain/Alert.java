/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class Alert extends Alert_Base {

    public Alert() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRootDomainObjectForActiveAlerts(Bennu.getInstance());
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
    public boolean hasBennu() {
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
    public boolean hasBennuForActiveAlerts() {
        return getRootDomainObjectForActiveAlerts() != null;
    }

    @Deprecated
    public boolean hasFireDate() {
        return getFireDate() != null;
    }

}
