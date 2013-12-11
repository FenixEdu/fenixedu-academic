package net.sourceforge.fenixedu.domain.mobility.outbound;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.BundleUtil;
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
        setOutboundMobilityCandidacySubmission(null);
        setOutboundMobilityCandidacyContest(null);
        setRootDomainObject(null);
        deleteDomainObject();
        if (submission.hasAnyOutboundMobilityCandidacy()) {
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
            final Recipient recipient = new Recipient(new PersonGroup(registration.getPerson()));
            new Message(sender, recipient, BundleUtil.getStringFromResourceBundle("resources.StudentResources",
                    "label.email.deleted.contest.subject"), BundleUtil.getStringFromResourceBundle("resources.StudentResources",
                    "label.email.deleted.contest.body", getOutboundMobilityCandidacyContest().getMobilityAgreement()
                            .getUniversityUnit().getPresentationName()));
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

    @Deprecated
    public boolean hasPreferenceOrder() {
        return getPreferenceOrder() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmission() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSubmissionFromSelectedCandidacy() {
        return getSubmissionFromSelectedCandidacy() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyContest() {
        return getOutboundMobilityCandidacyContest() != null;
    }

    @Deprecated
    public boolean hasSelected() {
        return getSelected() != null;
    }

}
