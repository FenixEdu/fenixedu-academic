package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

import org.joda.time.DateTime;
import org.joda.time.Days;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class NotUpdatedAlumniInfoForSpecificTimeGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;
    private int daysNotUpdated;
    private boolean checkJobNotUpdated;
    private boolean checkFormationNotUpdated;

    public NotUpdatedAlumniInfoForSpecificTimeGroup(Integer daysNotUpdated, boolean checkJobNotUpdated,
	    boolean checkFormationNotUpdated) {
	setDaysNotUpdated(daysNotUpdated);
	setCheckJobNotUpdated(checkJobNotUpdated);
	setCheckFormationNotUpdated(checkFormationNotUpdated);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	DateTime now = new DateTime();
	boolean continueCicle = false;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
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
	    if (isCheckFormationNotUpdated()) {
		for (Formation formation : alumni.getFormations()) {
		    if (formation.getLastModificationDateDateTime() == null
			    || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now, formation
				    .getLastModificationDateDateTime()))) {
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
			    || (formation.getLastModificationDateDateTime() != null && notRecentlyUpdated(now, formation
				    .getLastModificationDateDateTime()))) {
			return true;
		    }
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
	return new Argument[] { numberOperator, jobArgument, formationArgument };
    }

    @Override
    public String getName() {
	return RenderUtils.getFormatedResourceString("GROUP_NAME_RESOURCES", "label.name.alumniInfoNotUpdated",
		getDaysNotUpdated());
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    Integer daysOperator;
	    String jobArgument;
	    String formationArgument;
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
	    return new NotUpdatedAlumniInfoForSpecificTimeGroup(daysOperator, Boolean.valueOf(jobArgument), Boolean
		    .valueOf(formationArgument));
	}

	public int getMinArguments() {
	    return 3;
	}

	public int getMaxArguments() {
	    return 3;
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
}