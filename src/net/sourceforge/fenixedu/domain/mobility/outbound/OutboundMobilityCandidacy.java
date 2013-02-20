package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class OutboundMobilityCandidacy extends OutboundMobilityCandidacy_Base implements Comparable<OutboundMobilityCandidacy> {

    public OutboundMobilityCandidacy(final OutboundMobilityCandidacyContest outboundMobilityCandidacyContest,
            final OutboundMobilityCandidacySubmission outboundMobilityCandidacySubmission) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOutboundMobilityCandidacyContest(outboundMobilityCandidacyContest);
        setOutboundMobilityCandidacySubmission(outboundMobilityCandidacySubmission);
        setPreferenceOrder(outboundMobilityCandidacySubmission.getOutboundMobilityCandidacyCount());
        setSelected(Boolean.FALSE);
    }

    @Service
    public void delete() {
        final OutboundMobilityCandidacySubmission submission = getOutboundMobilityCandidacySubmission();
        if (!submission.getOutboundMobilityCandidacyPeriod().isAcceptingCandidacies()) {
            throw new DomainException("error.CandidacyPeriod.closed");
        }
        removeOutboundMobilityCandidacySubmission();
        removeOutboundMobilityCandidacyContest();
        removeRootDomainObject();
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

    @Service
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
        int p = getPreferenceOrder().compareTo(o.getPreferenceOrder());
        return p == 0 ? getExternalId().compareTo(o.getExternalId()) : p;
    }

    public void deleteWithNotification() {
        final SystemSender sender = getRootDomainObject().getSystemSender();
        if (sender != null) {
            final Registration registration = getOutboundMobilityCandidacySubmission().getRegistration();
            final Recipient recipient = new Recipient(new PersonGroup(registration.getPerson()));
            new Message(sender, recipient,
                    BundleUtil.getStringFromResourceBundle("resources.StudentResources", "label.email.deleted.contest.subject"),
                    BundleUtil.getStringFromResourceBundle("resources.StudentResources", "label.email.deleted.contest.body", getOutboundMobilityCandidacyContest().getMobilityAgreement().getUniversityUnit().getPresentationName()));
        }
        delete();
    }

}
