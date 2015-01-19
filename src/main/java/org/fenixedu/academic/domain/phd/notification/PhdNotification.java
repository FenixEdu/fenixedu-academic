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
package org.fenixedu.academic.domain.phd.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PhdNotification extends PhdNotification_Base {

    public static final Comparator<PhdNotification> COMPARATOR_BY_NUMBER = new Comparator<PhdNotification>() {
        @Override
        public int compare(PhdNotification left, PhdNotification right) {

            int result = left.getNumber().compareTo(right.getNumber());

            return result == 0 ? left.getExternalId().compareTo(right.getExternalId()) : result;

        };
    };

    public PhdNotification() {
        super();
        setWhenCreated(new DateTime());
        if (AccessControl.getPerson() != null) {
            setCreatedBy(AccessControl.getPerson().getUsername());
        }
    }

    public PhdNotification(PhdNotificationType type, PhdProgramCandidacyProcess candidacyProcess) {
        this();
        init(type, candidacyProcess);
    }

    public PhdNotification(PhdNotificationBean bean) {
        this(bean.getType(), bean.getCandidacyProcess());
    }

    protected void init(PhdNotificationType type, PhdProgramCandidacyProcess candidacyProcess) {
        String[] args = {};
        if (type == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.notification.PhdNotification.type.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (candidacyProcess == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.phd.notification.PhdNotification.candidacyProcess.cannot.be.null", args1);
        }

        super.setType(type);
        super.setYear(new LocalDate().getYear());
        super.setNumber(generateNumber(getYear()));
        super.setRootDomainObject(Bennu.getInstance());
        super.setCandidacyProcess(candidacyProcess);
        super.setState(PhdNotificationState.EMITTED);

    }

    @Override
    public void setType(PhdNotificationType type) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.notification.PhdNotification.cannot.modify.type");
    }

    @Override
    public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.phd.notification.PhdNotification.cannot.modify.candidacyProcess");
    }

    @Override
    public void setState(PhdNotificationState state) {
        throw new DomainException("error.org.fenixedu.academic.domain.phd.notification.PhdNotification.cannot.modify.state");
    }

    private Integer generateNumber(int year) {
        final List<PhdNotification> notifications = getNotificationsForYear(year);
        return notifications.isEmpty() ? 1 : Collections.max(notifications, PhdNotification.COMPARATOR_BY_NUMBER).getNumber() + 1;
    }

    private List<PhdNotification> getNotificationsForYear(int year) {
        final List<PhdNotification> result = new ArrayList<PhdNotification>();

        for (final PhdNotification each : Bennu.getInstance().getPhdNotificationsSet()) {
            if (each.isFor(year)) {
                result.add(each);
            }
        }

        return result;
    }

    private boolean isFor(int year) {
        return getYear().intValue() == year;
    }

    public String getNotificationNumber() {
        return getNumber() + "/" + getYear();
    }

    @Atomic
    public void markAsSent() {
        super.setState(PhdNotificationState.SENT);
    }

    public boolean isSent() {
        return getState() == PhdNotificationState.SENT;
    }

}
