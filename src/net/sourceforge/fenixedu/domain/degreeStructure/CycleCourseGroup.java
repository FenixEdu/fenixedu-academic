package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;

public class CycleCourseGroup extends CycleCourseGroup_Base {

    protected CycleCourseGroup() {
	super();
    }

    public CycleCourseGroup(final RootCourseGroup parentCourseGroup, final String name, final String nameEn,
	    final CycleType cycleType, final ExecutionPeriod begin, final ExecutionPeriod end) {
	if (cycleType == null) {
	    throw new DomainException("error.degreeStructure.CycleCourseGroup.cycle.type.cannot.be.null");
	}
	init(parentCourseGroup, name, nameEn, begin, end);
	setCycleType(cycleType);
    }

    @Override
    public void delete() {
	getSourceAffinitiesSet().clear();
	getDestinationAffinitiesSet().clear();
	super.delete();
    }

    @Override
    public boolean isCycleCourseGroup() {
	return true;
    }

    final public String getGraduateTitle() {
	final StringBuilder result = new StringBuilder();

	result.append(getDegreeType().getGraduateTitle(getCycleType()));

	final String degreeFilteredName = getDegree().getFilteredName();
	final String in = ResourceBundle.getBundle("resources/ApplicationResources", LanguageUtils.getLocale()).getString(
		"label.in");
	result.append(" ").append(in);

	final String graduateTitleSuffix = getGraduateTitleSuffix();
	if (!StringUtils.isEmpty(graduateTitleSuffix) && !degreeFilteredName.contains(graduateTitleSuffix.trim())) {
	    result.append(" ").append(graduateTitleSuffix);
	    result.append(" -");
	}

	result.append(" ").append(degreeFilteredName);

	return result.toString();
    }

    public boolean isFirstCycle() {
	return getCycleType() == CycleType.FIRST_CYCLE;
    }

    public boolean isSecondCycle() {
	return getCycleType() == CycleType.SECOND_CYCLE;
    }

    public boolean isThirdCycle() {
	return getCycleType() == CycleType.THIRD_CYCLE;
    }

}
