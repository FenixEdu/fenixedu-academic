/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.alert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdAlertMessage extends PhdAlertMessage_Base {

    static final public Comparator<PhdAlertMessage> COMPARATOR_BY_WHEN_CREATED_AND_ID = new Comparator<PhdAlertMessage>() {
        @Override
        public int compare(PhdAlertMessage m1, PhdAlertMessage m2) {
            int comp = m1.getWhenCreated().compareTo(m2.getWhenCreated());
            return (comp != 0) ? comp : DomainObjectUtil.COMPARATOR_BY_ID.compare(m1, m2);
        }
    };

    protected PhdAlertMessage() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    public PhdAlertMessage(PhdIndividualProgramProcess process, Person person, MultiLanguageString subject,
            MultiLanguageString body) {
        this();
        init(process, Collections.singletonList(person), subject, body);
    }

    public PhdAlertMessage(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
            MultiLanguageString body) {
        this();
        init(process, persons, subject, body);
    }

    protected void init(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
            MultiLanguageString body) {
        checkParameters(process, persons, subject, body);
        super.setProcess(process);
        super.getPersonsSet().addAll(persons);
        super.setSubject(subject);
        super.setBody(body);
        super.setReaded(Boolean.FALSE);
    }

    private void checkParameters(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
            MultiLanguageString body) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.process.cannot.be.null", args);
        }
        String[] args1 = {};
        if (persons == null) {
            throw new DomainException("error.phd.alert.PhdAlertMessage.persons.cannot.be.empty", args1);
        }
        String[] args2 = {};
        if (subject == null) {
            throw new DomainException("error.phd.alert.PhdAlertMessage.subject.cannot.be.null", args2);
        }
        String[] args3 = {};
        if (body == null) {
            throw new DomainException("error.phd.alert.PhdAlertMessage.body.cannot.be.null", args3);
        }
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.modify.process");
    }

    @Override
    public void addPersons(Person person) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.add.person");
    }

    @Override
    public Set<Person> getPersonsSet() {
        return Collections.unmodifiableSet(super.getPersonsSet());
    }

    @Override
    public void removePersons(Person person) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.remove.person");
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.modify.subject");
    }

    @Override
    public void setBody(MultiLanguageString body) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.modify.body");
    }

    @Override
    public void setReaded(Boolean readed) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.cannot.modify.readed");
    }

    @Atomic
    public void markAsReaded(Person person) {
        String[] args = {};
        if (person == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.phd.alert.PhdAlertMessage.personWhoMarkAsReaded.cannot.be.null", args);
        }

        super.setReaded(true);
        super.setPersonWhoMarkedAsReaded(person);
    }

    @Atomic
    public void markAsUnread() {
        super.setReaded(false);
        setPersonWhoMarkedAsReaded(null);
    }

    public boolean isReaded() {
        return getReaded().booleanValue();
    }

    public boolean isFor(Person person) {
        return getPersonsSet().contains(person);
    }

    public List<PhdAlert> getAlertsPossibleResponsibleForMessageGeneration() {
        List<PhdAlert> result = new ArrayList<PhdAlert>();
        Collection<PhdAlert> alerts = getProcess().getAlertsSet();

        for (PhdAlert phdAlert : alerts) {
            if (getSubject().getContent().contentEquals(phdAlert.getFormattedSubject().getContent())) {
                result.add(phdAlert);
            }
        }

        return result;
    }

    protected UnitBasedSender getSender() {
        AdministrativeOffice administrativeOffice = this.getProcess().getAdministrativeOffice();
        return administrativeOffice.getUnit().getUnitBasedSenderSet().iterator().next();
    }

    public List<Message> getEmailsWithMatchWithThisMessage() {
        List<Message> result = new ArrayList<Message>();

        UnitBasedSender sender = getSender();

        Collection<Message> messages = sender.getMessagesSet();

        for (Message message : messages) {
            if (getSubject().getContent().contentEquals(message.getSubject())) {
                result.add(message);
            }
        }

        return result;
    }

}
