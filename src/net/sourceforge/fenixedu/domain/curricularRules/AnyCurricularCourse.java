/*
 * Created on Feb 17, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class AnyCurricularCourse extends AnyCurricularCourse_Base {
    
    protected AnyCurricularCourse(final OptionalCurricularCourse toApplyRule,
	    final CourseGroup contextCourseGroup, final ExecutionPeriod begin,
	    final ExecutionPeriod end, final Double credits, final Integer curricularPeriodOrder,
	    final Integer minimumYear, final Integer maximumYear, final DegreeType degreeType,
	    final Degree degree, final Unit departmentUnit) {
        
        super();
        init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.ANY_CURRICULAR_COURSE);
        
        checkYears(minimumYear, maximumYear);
        
        setCredits(credits);
        setCurricularPeriodOrder(curricularPeriodOrder);
        setMinimumYear(minimumYear);
        setMaximumYear(maximumYear);
        setBolonhaDegreeType(degreeType);
        setDegree(degree);
        setDepartmentUnit(departmentUnit);
    }
    
    protected void edit(final CourseGroup contextCourseGroup, final Double credits,
	    final Integer curricularPeriodOrder, final Integer minimumYear, final Integer maximumYear,
	    final DegreeType degreeType, final Degree degree, final Unit departmentUnit) {

	checkYears(minimumYear, maximumYear);
	setContextCourseGroup(contextCourseGroup);
	setCredits(credits);
	setCurricularPeriodOrder(curricularPeriodOrder);
	setMinimumYear(minimumYear);
	setMaximumYear(maximumYear);
	setBolonhaDegreeType(degreeType);
	setDegree(degree);
	setDepartmentUnit(departmentUnit);
    }
    
    private void checkYears(Integer minimumYear, Integer maximumYear) throws DomainException {
        if (minimumYear != null && maximumYear != null && minimumYear.intValue() > maximumYear.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }
    
    @Override
    public OptionalCurricularCourse getDegreeModuleToApplyRule() {
        return (OptionalCurricularCourse) super.getDegreeModuleToApplyRule();
    }
    
    @Override
    public boolean appliesToContext(final Context context) {
        return super.appliesToContext(context) && appliesToPeriod(context) && appliesToYears(context);
    }

    private boolean appliesToPeriod(final Context context) {
	return !hasCurricularPeriodOrder() || hasCurricularPeriodOrderFor(context);
    }
    
    private boolean hasCurricularPeriodOrderFor(final Context context) {
	return context.containsSemester(getCurricularPeriodOrder())
		&& context.getCurricularPeriod().getPeriodType() == CurricularPeriodType.SEMESTER;
    }

    private boolean hasCurricularPeriodOrder() {
	return getCurricularPeriodOrder() != null && getCurricularPeriodOrder().intValue() != 0;
    }
    
    private boolean hasYearsLimit() {
	return getMinimumYear() != null && getMinimumYear().intValue() != 0 && getMaximumYear() != null && getMaximumYear().intValue() != 0;
    }
    
    private boolean appliesToYears(final Context context) {
	if (!hasYearsLimit()) {
	    return true;
	}
	
	if (hasCurricularPeriodOrder()) {
	    for (int year = getMinimumYear().intValue() ; year <= getMaximumYear().intValue() ; year++) {
		if (context.containsSemesterAndCurricularYear(getCurricularPeriodOrder(), Integer.valueOf(year), RegimeType.SEMESTRIAL)) {
		    return true;
		}
	    }
	} else {
	    for (int year = getMinimumYear().intValue() ; year <= getMaximumYear().intValue() ; year++) {
		if (context.containsSemesterAndCurricularYear(Integer.valueOf(1), Integer.valueOf(year), RegimeType.SEMESTRIAL)
			|| context.containsSemesterAndCurricularYear(Integer.valueOf(2), Integer.valueOf(year), RegimeType.SEMESTRIAL)) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        
        labelList.add(new GenericPair<Object, Boolean>("label.anyCurricularCourse", true));
        
        if (getCredits() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.with", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCredits(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.credits", true));
        }
        
        if (getCurricularPeriodOrder().intValue() != 0) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
            labelList.add(new GenericPair<Object, Boolean>("º ", false));
            labelList.add(new GenericPair<Object, Boolean>("SEMESTER", true));
        }
        if (hasYearsLimit()) {
            if (getMinimumYear().compareTo(getMaximumYear()) == 0) {
                labelList.add(new GenericPair<Object, Boolean>(", ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.year", true));
            } else {
                labelList.add(new GenericPair<Object, Boolean>(", ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.to1", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMaximumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.year", true));
            }
        }
        
        labelList.add(new GenericPair<Object, Boolean>(", ", false));
        if (getDegree() == null) {
            if (!hasBolonhaDegreeType()) {
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>("institution.name.abbreviation", true));
            } else {
                labelList.add(new GenericPair<Object, Boolean>("label.of1", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getBolonhaDegreeType().name(), true));
            }
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.of", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.degree", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getDegree().getNome(), false));
        }
        
        if (getDepartmentUnit() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.of", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getDepartmentUnit().getName(), false));
        }

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));            
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }
        return labelList;
    }
    
    @Override
    protected void removeOwnParameters() {
        removeDegree();
        removeDepartmentUnit();
    }

    public boolean hasBolonhaDegreeType() {
	return getBolonhaDegreeType() != null;
    }
    
    public boolean hasCredits() {
	return getCredits() != null && getCredits().doubleValue() != 0d;
    }
}
