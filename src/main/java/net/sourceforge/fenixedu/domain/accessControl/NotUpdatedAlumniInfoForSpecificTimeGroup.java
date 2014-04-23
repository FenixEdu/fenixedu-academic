package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class NotUpdatedAlumniInfoForSpecificTimeGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;
    private int daysNotUpdated;
    private boolean checkJobNotUpdated;
    private boolean checkFormationNotUpdated;
    private boolean checkPersonalDataNotUpdated;

    public NotUpdatedAlumniInfoForSpecificTimeGroup(Integer daysNotUpdated, boolean checkJobNotUpdated,
            boolean checkFormationNotUpdated, boolean checkPersonalDataNotUpdated) {
        setDaysNotUpdated(daysNotUpdated);
        setCheckJobNotUpdated(checkJobNotUpdated);
        setCheckFormationNotUpdated(checkFormationNotUpdated);
        setCheckPersonalDataNotUpdated(checkPersonalDataNotUpdated);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        DateTime now = new DateTime();
        boolean continueCicle = false;
        for (Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
            if (isCheckJobNotUpdated()) {
                for (Job job : alumni.getJobs()) {
                    if (job.getLastModifiedDate() == null
                            || (job.getLastModifiedDate() != null && notRecentlyUpdated(now, job.getLastModifiedDate()))) {
                        elements.add(alumni.getStudent().getPerson());
                        continueCicle = true;
                        break;
                    }
                }
            }
            if (continueCicle) {
                continueCicle = false;
                continue;
            }
            if (isCheckPersonalDataNotUpdated()) {
                boolean areMailContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(EmailAddress.class, getDaysNotUpdated());
                boolean areMobileContactsRecent =
                        alumni.getStudent().getPerson().areContactsRecent(MobilePhone.class, getDaysNotUpdated());
                if (!areMailContactsRecent || !areMobileContactsRecent) {
                    elements.add(alumni.getStudent().getPerson());
                    continue;
                }
            }
            if (isCheckFormationNotUpdated()) {
                for (Formation formation : alumni.getFormations()) {
                    if (formation.getLastModificationDateDateTime() == null
                            || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now,
                                    formation.getLastModificationDateDateTime()))) {
                        elements.add(alumni.getStudent().getPerson());
                        break;
                    }
                }
            }
        }
        return super.freezeSet(elements);
    }

    private boolean notRecentlyUpdated(DateTime now, DateTime lastModifiedDate) {
        Days days = Days.daysBetween(lastModifiedDate, now);
        return days.getDays() > getDaysNotUpdated();
    }

    @Override
    public boolean isMember(Person person) {
        DateTime now = new DateTime();
        if (person != null && person.hasStudent() && person.getStudent().hasAlumni()) {
            if (isCheckJobNotUpdated()) {
                for (Job job : person.getJobs()) {
                    if (job.getLastModifiedDate() == null
                            || (job.getLastModifiedDate() != null && notRecentlyUpdated(now, job.getLastModifiedDate()))) {
                        return true;
                    }
                }
            }
            if (isCheckFormationNotUpdated()) {
                for (Formation formation : person.getFormations()) {
                    if (formation.getLastModificationDateDateTime() == null
                            || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now,
                                    formation.getLastModificationDateDateTime()))) {
                        return true;
                    }
                }
            }
            if (isCheckPersonalDataNotUpdated()) {
                boolean areMailContactsRecent = person.areContactsRecent(EmailAddress.class, getDaysNotUpdated());
                boolean areMobileContactsRecent = person.areContactsRecent(MobilePhone.class, getDaysNotUpdated());
                if (!areMailContactsRecent || !areMobileContactsRecent) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        StaticArgument numberOperator = new StaticArgument(getDaysNotUpdated());
        StaticArgument jobArgument = new StaticArgument(isCheckJobNotUpdated());
        StaticArgument formationArgument = new StaticArgument(isCheckFormationNotUpdated());
        StaticArgument personalDataArgument = new StaticArgument(isCheckPersonalDataNotUpdated());
        return new Argument[] { numberOperator, jobArgument, formationArgument, personalDataArgument };
    }

    @Override
    public String getName() {
        String[] args = new String[4];
        String key = "label.name.alumniInfoNotUpdated.oneItem";
        int iter = 0;
        if (isCheckFormationNotUpdated()) {
            args[iter] = BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.formationInfo");
            iter++;
        }
        if (isCheckJobNotUpdated()) {
            args[iter] = BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.jobInfo");
            iter++;
        }
        if (isCheckPersonalDataNotUpdated()) {
            args[iter] =
                    BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), "label.name.alumni.personalDataInfo");
            iter++;
        }
        if (iter == 2) {
            key = "label.name.alumniInfoNotUpdated.twoItems";
        } else if (iter == 3) {
            key = "label.name.alumniInfoNotUpdated.threeItems";
        }
        args[iter] = getDaysNotUpdated().toString();
        return BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), key, args);
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            Integer daysOperator;
            String jobArgument;
            String formationArgument;
            String personalDataArgument;
            try {
                daysOperator = (Integer) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, StaticArgument.class, arguments[0].getClass());
            }
            try {
                jobArgument = (String) arguments[1];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, StaticArgument.class, arguments[1].getClass());
            }
            try {
                formationArgument = (String) arguments[2];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(2, StaticArgument.class, arguments[2].getClass());
            }
            try {
                personalDataArgument = (String) arguments[3];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(3, StaticArgument.class, arguments[3].getClass());
            }
            return new NotUpdatedAlumniInfoForSpecificTimeGroup(daysOperator, Boolean.valueOf(jobArgument),
                    Boolean.valueOf(formationArgument), Boolean.valueOf(personalDataArgument));
        }

        @Override
        public int getMinArguments() {
            return 4;
        }

        @Override
        public int getMaxArguments() {
            return 4;
        }
    }

    public Integer getDaysNotUpdated() {
        return daysNotUpdated;
    }

    public void setDaysNotUpdated(Integer daysNotUpdated) {
        this.daysNotUpdated = daysNotUpdated;
    }

    public boolean isCheckJobNotUpdated() {
        return checkJobNotUpdated;
    }

    public void setCheckJobNotUpdated(boolean checkJobNotUpdated) {
        this.checkJobNotUpdated = checkJobNotUpdated;
    }

    public boolean isCheckFormationNotUpdated() {
        return checkFormationNotUpdated;
    }

    public void setCheckFormationNotUpdated(boolean checkFormationNotUpdated) {
        this.checkFormationNotUpdated = checkFormationNotUpdated;
    }

    public void setDaysNotUpdated(int daysNotUpdated) {
        this.daysNotUpdated = daysNotUpdated;
    }

    public boolean isCheckPersonalDataNotUpdated() {
        return checkPersonalDataNotUpdated;
    }

    public void setCheckPersonalDataNotUpdated(boolean checkPersonalDataNotUpdated) {
        this.checkPersonalDataNotUpdated = checkPersonalDataNotUpdated;
    }

    @Override
    public PersistentNotUpdatedAlumniInfoForSpecificDaysGroup convert() {
        return PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.getInstance(daysNotUpdated, checkJobNotUpdated,
                checkFormationNotUpdated, checkPersonalDataNotUpdated);
    }
}