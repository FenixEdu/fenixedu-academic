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
package org.fenixedu.academic.domain.mobility.outbound;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacy extends OutboundMobilityCandidacy_Base implements Comparable<OutboundMobilityCandidacy> {

    public OutboundMobilityCandidacy(final OutboundMobilityCandidacyContest outboundMobilityCandidacyContest,
            final OutboundMobilityCandidacySubmission outboundMobilityCandidacySubmission) {
        setRootDomainObject(Bennu.getInstance());
        setOutboundMobilityCandidacyContest(outboundMobilityCandidacyContest);
        setOutboundMobilityCandidacySubmission(outboundMobilityCandidacySubmission);
        setPreferenceOrder(outboundMobilityCandidacySubmission.getOutboundMobilityCandidacySet().size());
        setSelected(Boolean.FALSE);
    }

    @Atomic
    public void delete() {
        final OutboundMobilityCandidacySubmission submission = getOutboundMobilityCandidacySubmission();
        if (!submission.getOutboundMobilityCandidacyPeriod().isAcceptingCandidacies()) {
            throw new DomainException("error.CandidacyPeriod.closed");
        }
        if (getSubmissionFromSelectedCandidacy() != null) {
            throw new DomainException("error.SubmissionFromSelectedCandidacy.not.empty");
        }
        setOutboundMobilityCandidacySubmission(null);
        setOutboundMobilityCandidacyContest(null);
        setRootDomainObject(null);
        deleteDomainObject();
        if (!submission.getOutboundMobilityCandidacySet().isEmpty()) {
            int i = 0;
            for (final OutboundMobilityCandidacy candidacy : submission.getSortedOutboundMobilityCandidacySet()) {
                candidacy.setPreferenceOrder(Integer.valueOf(++i));
            }
        } else {
            submission.delete();
        }
    }

    @Atomic
    public void reorder(final int index) {
        final int currentOrder = getPreferenceOrder().intValue();
        if (index != currentOrder) {
            final OutboundMobilityCandidacySubmission submission = getOutboundMobilityCandidacySubmission();
            if (index < currentOrder) {
                for (final OutboundMobilityCandidacy candidacy : submission.getSortedOutboundMobilityCandidacySet()) {
                    final int order = candidacy.getPreferenceOrder().intValue();
                    if (order >= index && order < currentOrder) {
                        candidacy.setPreferenceOrder(Integer.valueOf(order + 1));
                    }
                }
            } else {
                for (final OutboundMobilityCandidacy candidacy : submission.getSortedOutboundMobilityCandidacySet()) {
                    final int order = candidacy.getPreferenceOrder().intValue();
                    if (order <= index && order > currentOrder) {
                        candidacy.setPreferenceOrder(Integer.valueOf(order - 1));
                    }
                }
            }
            setPreferenceOrder(Integer.valueOf(index));
        }
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacy o) {
        final int s = getOutboundMobilityCandidacySubmission().compareTo(o.getOutboundMobilityCandidacySubmission());
        if (s != 0) {
            return s;
        }
        final int p = getPreferenceOrder().compareTo(o.getPreferenceOrder());
        return p == 0 ? getExternalId().compareTo(o.getExternalId()) : p;
    }

    public void deleteWithNotification() {
        final SystemSender sender = getRootDomainObject().getSystemSender();
        if (sender != null) {
            final Registration registration = getOutboundMobilityCandidacySubmission().getRegistration();
            final Recipient recipient = new Recipient(UserGroup.of(registration.getPerson().getUser()));
            new Message(sender, recipient, BundleUtil.getString(Bundle.STUDENT, "label.email.deleted.contest.subject"),
                    BundleUtil.getString(Bundle.STUDENT, "label.email.deleted.contest.body",
                            getOutboundMobilityCandidacyContest().getMobilityAgreement().getUniversityUnit()
                                    .getPresentationName()));
        }
        delete();
    }

    @Atomic
    public void select() {
        final OutboundMobilityCandidacySubmission submission = getOutboundMobilityCandidacySubmission();
        if (submission.getSelectedCandidacy() != this) {
            if (submission.getSelectedCandidacy() != null) {
                throw new DomainException("error.message.cannot.select.multiple.candidacies", submission.getRegistration()
                        .getPerson().getUsername());
            }

            final OutboundMobilityCandidacyContest contest = getOutboundMobilityCandidacyContest();
            if (contest.hasVacancy()) {
                setSelected(Boolean.TRUE);
                submission.setSelectedCandidacy(this);
            } else {
                throw new DomainException("error.message.contest.has.no.more.vacancies");
            }
        }
    }

    @Atomic
    public void unselect() {
        setSelected(Boolean.FALSE);
        setSubmissionFromSelectedCandidacy(null);
    }

}
