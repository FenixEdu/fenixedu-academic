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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.google.common.base.Objects;

@GroupOperator("notUpdatedAlumniInfoForSpecificDays")
public class NotUpdatedAlumniInfoForSpecificDaysGroup extends FenixGroup {
    private static final long serialVersionUID = -2553217955281571304L;

    @GroupArgument
    private Integer daysNotUpdated;

    @GroupArgument
    private Boolean checkJobNotUpdated;

    @GroupArgument
    private Boolean checkFormationNotUpdated;

    @GroupArgument
    private Boolean checkPersonalDataNotUpdated;

    private NotUpdatedAlumniInfoForSpecificDaysGroup() {
        super();
    }

    private NotUpdatedAlumniInfoForSpecificDaysGroup(Integer daysNotUpdated, Boolean checkJobNotUpdated,
            Boolean checkFormationNotUpdated, Boolean checkPersonalDataNotUpdated) {
        this();
        this.daysNotUpdated = daysNotUpdated;
        this.checkJobNotUpdated = checkJobNotUpdated;
        this.checkFormationNotUpdated = checkFormationNotUpdated;
        this.checkPersonalDataNotUpdated = checkPersonalDataNotUpdated;
    }

    public static NotUpdatedAlumniInfoForSpecificDaysGroup get(Integer daysNotUpdated, Boolean checkJobNotUpdated,
            Boolean checkFormationNotUpdated, Boolean checkPersonalDataNotUpdated) {
        return new NotUpdatedAlumniInfoForSpecificDaysGroup(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated,
                checkPersonalDataNotUpdated);
    }

    @Override
    public String getPresentationName() {
        String[] args = new String[4];
        String key = "label.name.alumniInfoNotUpdated.oneItem";
        int iter = 0;
        if (checkFormationNotUpdated) {
            args[iter] = BundleUtil.getString(getPresentationNameBundle(), "label.name.alumni.formationInfo");
            iter++;
        }
        if (checkJobNotUpdated) {
            args[iter] = BundleUtil.getString(getPresentationNameBundle(), "label.name.alumni.jobInfo");
            iter++;
        }
        if (checkPersonalDataNotUpdated) {
            args[iter] =
                    BundleUtil.getString(getPresentationNameBundle(), "label.name.alumni.personalDataInfo");
            iter++;
        }
        if (iter == 2) {
            key = "label.name.alumniInfoNotUpdated.twoItems";
        } else if (iter == 3) {
            key = "label.name.alumniInfoNotUpdated.threeItems";
        }
        args[iter] = Integer.toString(daysNotUpdated);
        return BundleUtil.getString(getPresentationNameBundle(), key, args);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        DateTime now = new DateTime();
        boolean continueCicle = false;
        for (Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
            if (checkJobNotUpdated) {
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
            if (checkPersonalDataNotUpdated) {
                boolean areMailContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(EmailAddress.class, daysNotUpdated);
                boolean areMobileContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(MobilePhone.class, daysNotUpdated);
                if (!areMailContactsRecent || !areMobileContactsRecent) {
                    User user = alumni.getStudent().getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                        continue;
                    }
                }
            }
            if (checkFormationNotUpdated) {
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
        if (checkJobNotUpdated) {
            for (Job job : user.getPerson().getJobsSet()) {
                if (job.getLastModifiedDate() == null
                        || (job.getLastModifiedDate() != null && notRecentlyUpdated(now, job.getLastModifiedDate()))) {
                    return true;
                }
            }
        }
        if (checkFormationNotUpdated) {
            for (Formation formation : user.getPerson().getFormations()) {
                if (formation.getLastModificationDateDateTime() == null
                        || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now,
                                formation.getLastModificationDateDateTime()))) {
                    return true;
                }
            }
        }
        if (checkPersonalDataNotUpdated) {
            boolean areMailContactsRecent = user.getPerson().areContactsRecent(EmailAddress.class, daysNotUpdated);
            boolean areMobileContactsRecent = user.getPerson().areContactsRecent(MobilePhone.class, daysNotUpdated);
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
        return days.getDays() > daysNotUpdated;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.getInstance(daysNotUpdated, checkJobNotUpdated,
                checkFormationNotUpdated, checkPersonalDataNotUpdated);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NotUpdatedAlumniInfoForSpecificDaysGroup) {
            NotUpdatedAlumniInfoForSpecificDaysGroup other = (NotUpdatedAlumniInfoForSpecificDaysGroup) object;
            return Objects.equal(daysNotUpdated, other.daysNotUpdated)
                    && Objects.equal(checkJobNotUpdated, other.checkJobNotUpdated)
                    && Objects.equal(checkFormationNotUpdated, other.checkFormationNotUpdated)
                    && Objects.equal(checkPersonalDataNotUpdated, other.checkPersonalDataNotUpdated);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
    }
}
