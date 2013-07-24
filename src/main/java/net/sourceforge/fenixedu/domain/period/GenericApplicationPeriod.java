package net.sourceforge.fenixedu.domain.period;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        setPeriodNumber(RootDomainObject.getInstance().getCandidacyPeriodsCount());
        init(ExecutionYear.readCurrentExecutionYear(), start, end);
    }

    public String generateApplicationNumber() {
        return "C" + new LocalDate().getYear() + "/" + getPeriodNumber() + "/" + (getGenericApplicationCount() + 1);
    }

    public static SortedSet<GenericApplicationPeriod> getPeriods() {
        final SortedSet<GenericApplicationPeriod> result =
                new TreeSet<GenericApplicationPeriod>(GenericApplicationPeriod.COMPARATOR_BY_DATES);
        for (final CandidacyPeriod candidacyPeriod : RootDomainObject.getInstance().getCandidacyPeriods()) {
            if (candidacyPeriod instanceof GenericApplicationPeriod) {
                result.add((GenericApplicationPeriod) candidacyPeriod);
            }

        }
        return result;
    }

    @Atomic
    public GenericApplication createApplication(final String email) {
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
        final IUserView userView = AccessControl.getUserView();
        return userView != null
                && (userView.hasRoleType(RoleType.MANAGER) || getManagerSet().contains(userView.getPerson().getUser()));
    }

}
