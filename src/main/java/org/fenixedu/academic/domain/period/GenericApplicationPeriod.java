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
package org.fenixedu.academic.domain.period;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacy.GenericApplication;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationPeriod extends GenericApplicationPeriod_Base {

    public static final Comparator<GenericApplicationPeriod> COMPARATOR_BY_DATES = new Comparator<GenericApplicationPeriod>() {
        @Override
        public int compare(final GenericApplicationPeriod o1, final GenericApplicationPeriod o2) {
            final int s = o1.getStart().compareTo(o2.getStart());
            if (s == 0) {
                final int e = o1.getEnd().compareTo(o2.getEnd());
                return e == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : e;
            }
            return s;
        }
    };

    public GenericApplicationPeriod(final MultiLanguageString title, final MultiLanguageString description, final DateTime start,
            final DateTime end) {
        super();
        setTitle(title);
        setDescription(description);
        setPeriodNumber(Bennu.getInstance().getCandidacyPeriodsSet().size());
        init(ExecutionYear.readCurrentExecutionYear(), start, end);
    }

    public String generateApplicationNumber() {
        return "C" + new LocalDate().getYear() + "/" + getPeriodNumber() + "/" + (getGenericApplicationSet().size() + 1);
    }

    public static SortedSet<GenericApplicationPeriod> getPeriods() {
        final SortedSet<GenericApplicationPeriod> result =
                new TreeSet<GenericApplicationPeriod>(GenericApplicationPeriod.COMPARATOR_BY_DATES);
        for (final CandidacyPeriod candidacyPeriod : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (candidacyPeriod instanceof GenericApplicationPeriod) {
                result.add((GenericApplicationPeriod) candidacyPeriod);
            }
        }
        return result;
    }

    @Atomic
    public GenericApplication createApplication(final String email) {
        if (this.getStart().isAfterNow() || this.getEnd().isBeforeNow()) {
            throw new DomainException("message.application.submission.period.ended");
        }
        for (final GenericApplication genericApplication : getGenericApplicationSet()) {
            if (genericApplication.getEmail().equalsIgnoreCase(email)) {
                genericApplication.sendEmailForApplication();
                return genericApplication;
            }
        }
        return new GenericApplication(this, email);
    }

    public SortedSet<GenericApplication> getOrderedGenericApplicationSet() {
        final SortedSet<GenericApplication> result =
                new TreeSet<GenericApplication>(GenericApplication.COMPARATOR_BY_APPLICATION_NUMBER);
        result.addAll(getGenericApplicationSet());
        return result;
    }

    @Atomic
    public void removeManagerService(final User user) {
        if (isCurrentUserAllowedToMange()) {
            removeManager(user);
        }
    }

    public boolean isCurrentUserAllowedToMange() {
        final User userView = Authenticate.getUser();
        return userView != null
                && (RoleType.MANAGER.isMember(userView.getPerson().getUser()) || getManagerSet().contains(
                        userView.getPerson().getUser()));
    }

}
