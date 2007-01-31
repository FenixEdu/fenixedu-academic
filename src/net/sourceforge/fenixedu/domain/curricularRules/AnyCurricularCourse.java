/*
 * Created on Feb 17, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class AnyCurricularCourse extends AnyCurricularCourse_Base {
    
    protected AnyCurricularCourse(CurricularCourse curricularCourseToApplyRule,
            CourseGroup contextCourseGroup, ExecutionPeriod begin, ExecutionPeriod end,
            Double credits, Integer curricularPeriodOrder, Integer minimumYear, Integer maximumYear, 
            DegreeType degreeType, Degree degree, Unit departmentUnit) {
        
        super();

        if (curricularCourseToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (curricularCourseToApplyRule.getType() != CurricularCourseType.OPTIONAL_COURSE) {
            throw new DomainException("curricular.rule.invalid.curricular.course.type");
        }
        
        checkExecutionPeriods(begin, end);
        checkYears(minimumYear, maximumYear);
        
        setDegreeModuleToApplyRule(curricularCourseToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(CurricularRuleType.ANY_CURRICULAR_COURSE);
        setCredits(credits);
        setCurricularPeriodOrder(curricularPeriodOrder);
        setMinimumYear(minimumYear);
        setMaximumYear(maximumYear);
        setBolonhaDegreeType(degreeType);
        setDegree(degree);
        setDepartmentUnit(departmentUnit);
    }
    
    protected void edit(CourseGroup contextCourseGroup, Double credits, Integer curricularPeriodOrder,
            Integer minimumYear, Integer maximumYear, DegreeType degreeType, Degree degree, Unit departmentUnit) {
        
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
        if (getMinimumYear() != null && getMaximumYear() != null) {
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
            if (getBolonhaDegreeType() == null) {
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>("IST", false));
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

    @Override
    public RuleResult evaluate(final EnrolmentContext enrolmentContext) {
        // TODO Auto-generated method stub
        /**
         * ? if getDegree() == null 
         *      ? getBolonhaDegreeType() == null ? any degree from IST
         *      ? getBolonhaDegreeType() != null ? any degree from DEGREE / MASTER_DEGREE / INTEGRATED_MASTER_DEGREE
         * ? else use selected degree
         * 
         * if departmentUnit != null ? curricular courses from competence courses that belong to that department      
         */
        return null;
    }
}
