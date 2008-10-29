package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CycleCourseGroup extends CycleCourseGroup_Base {

    protected CycleCourseGroup() {
	super();
    }

    public CycleCourseGroup(final RootCourseGroup parentCourseGroup, final String name, final String nameEn,
	    final CycleType cycleType, final ExecutionSemester begin, final ExecutionSemester end) {
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
	return getGraduateTitle(ExecutionYear.readCurrentExecutionYear(), Language.getLocale());
    }

    final public String getGraduateTitle(final ExecutionYear executionYear, final Locale locale) {
	final StringBuilder result = new StringBuilder();

	result.append(getDegreeType().getGraduateTitle(getCycleType(), locale));

	final String degreeFilteredName = getDegree().getFilteredName(executionYear, locale);
	result.append(StringUtils.SINGLE_SPACE).append(
		ResourceBundle.getBundle("resources/ApplicationResources", locale).getString("label.in"));

	final String graduateTitleSuffix = getGraduateTitleSuffix();
	if (!StringUtils.isEmpty(graduateTitleSuffix) && !degreeFilteredName.contains(graduateTitleSuffix.trim())) {
	    result.append(StringUtils.SINGLE_SPACE).append(graduateTitleSuffix);
	    result.append(StringUtils.SINGLE_SPACE).append("-");
	}

	result.append(StringUtils.SINGLE_SPACE).append(degreeFilteredName);

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

    @Override
    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
	return Collections.singletonList(this);
    }

    public Double getCurrentDefaultEcts() {
	return getDefaultEcts(ExecutionYear.readCurrentExecutionYear());
    }

    public Double getDefaultEcts(final ExecutionYear executionYear) {
	final CreditsLimit creditsLimit = (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT,
		null, executionYear);
	if (creditsLimit != null) {
	    return creditsLimit.getMinimumCredits();
	}

	if (getDegree().hasEctsCredits()) {
	    return getDegree().getEctsCredits();
	}

	if (getDegreeType().hasExactlyOneCycleType()) {
	    return getDegreeType().getDefaultEctsCredits();
	}

	throw new DomainException("error.CycleCourseGroup.cannot.calculate.default.ects.credits");
    }
}
