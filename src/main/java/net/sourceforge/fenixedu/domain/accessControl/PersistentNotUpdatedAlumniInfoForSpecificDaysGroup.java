package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;
import org.joda.time.Days;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;

@CustomGroupOperator("notUpdatedAlumniInfoForSpecificDays")
public class PersistentNotUpdatedAlumniInfoForSpecificDaysGroup extends PersistentNotUpdatedAlumniInfoForSpecificDaysGroup_Base {
    protected PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(Integer daysNotUpdated, Boolean checkJobNotUpdated,
            Boolean checkFormationNotUpdated, Boolean checkPersonalDataNotUpdated) {
        super();
        setDaysNotUpdated(daysNotUpdated);
        setCheckJobNotUpdated(checkJobNotUpdated);
        setCheckFormationNotUpdated(checkFormationNotUpdated);
        setCheckPersonalDataNotUpdated(checkPersonalDataNotUpdated);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Integer> daysNotUpdatedArgument() {
        return new SimpleArgument<Integer, PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
            @Override
            public Integer parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Integer.valueOf(argument);
            }

            @Override
            public Class<? extends Integer> getType() {
                return Integer.class;
            }

            @Override
            public String extract(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                return group.getDaysNotUpdated() != null ? Integer.toString(group.getDaysNotUpdated()) : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<Boolean> checkJobNotUpdatedArgument() {
        return new SimpleArgument<Boolean, PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
            @Override
            public Boolean parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Boolean.valueOf(argument);
            }

            @Override
            public Class<? extends Boolean> getType() {
                return Boolean.class;
            }

            @Override
            public String extract(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                return group.getCheckJobNotUpdated() != null ? Boolean.toString(group.getCheckJobNotUpdated()) : "";
            }
        };
    }

    @CustomGroupArgument(index = 3)
    public static Argument<Boolean> checkFormationNotUpdatedArgument() {
        return new SimpleArgument<Boolean, PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
            @Override
            public Boolean parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Boolean.valueOf(argument);
            }

            @Override
            public Class<? extends Boolean> getType() {
                return Boolean.class;
            }

            @Override
            public String extract(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                return group.getCheckFormationNotUpdated() != null ? Boolean.toString(group.getCheckFormationNotUpdated()) : "";
            }
        };
    }

    @CustomGroupArgument(index = 4)
    public static Argument<Boolean> checkPersonalDataNotUpdatedArgument() {
        return new SimpleArgument<Boolean, PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
            @Override
            public Boolean parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Boolean.valueOf(argument);
            }

            @Override
            public Class<? extends Boolean> getType() {
                return Boolean.class;
            }

            @Override
            public String extract(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                return group.getCheckPersonalDataNotUpdated() != null ? Boolean.toString(group.getCheckPersonalDataNotUpdated()) : "";
            }
        };
    }

    @Override
    public String getPresentationName() {
        String[] args = new String[4];
        String key = "label.name.alumniInfoNotUpdated.oneItem";
        int iter = 0;
        if (getCheckFormationNotUpdated()) {
            args[iter] = BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.formationInfo");
            iter++;
        }
        if (getCheckJobNotUpdated()) {
            args[iter] = BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.jobInfo");
            iter++;
        }
        if (getCheckPersonalDataNotUpdated()) {
            args[iter] =
                    BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.personalDataInfo");
            iter++;
        }
        if (iter == 2) {
            key = "label.name.alumniInfoNotUpdated.twoItems";
        } else if (iter == 3) {
            key = "label.name.alumniInfoNotUpdated.threeItems";
        }
        args[iter] = Integer.toString(getDaysNotUpdated());
        return BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), key, args);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        DateTime now = new DateTime();
        boolean continueCicle = false;
        for (Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
            if (getCheckJobNotUpdated()) {
                for (Job job : alumni.getJobs()) {
                    if (job.getLastModifiedDate() == null
                            || (job.getLastModifiedDate() != null && notRecentlyUpdated(now, job.getLastModifiedDate()))) {
                        User user = alumni.getStudent().getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                            continueCicle = true;
                            break;
                        }
                    }
                }
            }
            if (continueCicle) {
                continueCicle = false;
                continue;
            }
            if (getCheckPersonalDataNotUpdated()) {
                boolean areMailContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(EmailAddress.class, getDaysNotUpdated());
                boolean areMobileContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(MobilePhone.class, getDaysNotUpdated());
                if (!areMailContactsRecent || !areMobileContactsRecent) {
                    User user = alumni.getStudent().getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                        continue;
                    }
                }
            }
            if (getCheckFormationNotUpdated()) {
                for (Formation formation : alumni.getFormations()) {
                    if (formation.getLastModificationDateDateTime() == null
                            || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now,
                                    formation.getLastModificationDateDateTime()))) {
                        User user = alumni.getStudent().getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                            break;
                        }
                    }
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getStudent() == null || user.getPerson().getStudent().getAlumni() == null) {
            return false;
        }
        DateTime now = new DateTime();
        if (getCheckJobNotUpdated()) {
            for (Job job : user.getPerson().getJobsSet()) {
                if (job.getLastModifiedDate() == null
                        || (job.getLastModifiedDate() != null && notRecentlyUpdated(now, job.getLastModifiedDate()))) {
                    return true;
                }
            }
        }
        if (getCheckFormationNotUpdated()) {
            for (Formation formation : user.getPerson().getFormations()) {
                if (formation.getLastModificationDateDateTime() == null
                        || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now,
                                formation.getLastModificationDateDateTime()))) {
                    return true;
                }
            }
        }
        if (getCheckPersonalDataNotUpdated()) {
            boolean areMailContactsRecent = user.getPerson().areContactsRecent(EmailAddress.class, getDaysNotUpdated());
            boolean areMobileContactsRecent = user.getPerson().areContactsRecent(MobilePhone.class, getDaysNotUpdated());
            if (!areMailContactsRecent || !areMobileContactsRecent) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    private boolean notRecentlyUpdated(DateTime now, DateTime lastModifiedDate) {
        Days days = Days.daysBetween(lastModifiedDate, now);
        return days.getDays() > getDaysNotUpdated();
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentNotUpdatedAlumniInfoForSpecificDaysGroup getInstance(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> instance =
                select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
        return instance.isPresent() ? instance.get() : create(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated,
                checkPersonalDataNotUpdated);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentNotUpdatedAlumniInfoForSpecificDaysGroup create(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> instance =
                select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
        return instance.isPresent() ? instance.get() : new PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(daysNotUpdated,
                checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
    }

    private static Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> select(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        return filter(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.class).firstMatch(
                new Predicate<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
                    @Override
                    public boolean apply(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                        return group.getDaysNotUpdated() == daysNotUpdated && group.getCheckJobNotUpdated() == checkJobNotUpdated
                                && group.getCheckFormationNotUpdated() == checkFormationNotUpdated
                                && group.getCheckPersonalDataNotUpdated() == checkPersonalDataNotUpdated;
                    }
                });
    }
}
