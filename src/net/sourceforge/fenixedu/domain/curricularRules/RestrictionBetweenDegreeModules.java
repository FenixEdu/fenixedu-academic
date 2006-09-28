package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class RestrictionBetweenDegreeModules extends RestrictionBetweenDegreeModules_Base {

    private RestrictionBetweenDegreeModules(DegreeModule precedenceDegreeModule, Double minimumCredits) {
        super();
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimum(minimumCredits);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_BETWEEN_DEGREE_MODULES);
    }

    private void checkParameters(DegreeModule precedenceDegreeModule, Double minimumCredits) throws DomainException {
        if (precedenceDegreeModule == null || minimumCredits == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }
    
    protected RestrictionBetweenDegreeModules(DegreeModule degreeModuleToApplyRule, DegreeModule precedenceDegreeModule,
            Double minimumCredits, CourseGroup contextCourseGroup, ExecutionPeriod begin, ExecutionPeriod end) {
        
        this(precedenceDegreeModule, minimumCredits);
        
        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        
        checkExecutionPeriods(begin, end);
        
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setContextCourseGroup(contextCourseGroup);
    }
    
    protected void edit(DegreeModule precedenceDegreeModule, Double minimumCredits, CourseGroup contextCourseGroup) {
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimum(minimumCredits);
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        
        if (belongsToCompositeRule()
                && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedence", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedence", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.of", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.module", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        
        // getting full name only for course groups
        String precedenceDegreeModule = (getPrecedenceDegreeModule().isLeaf()) ? getPrecedenceDegreeModule().getName() : getPrecedenceDegreeModule().getOneFullName();
        labelList.add(new GenericPair<Object, Boolean>(precedenceDegreeModule, false));
        
        if (getMinimum().doubleValue() != 0.0) {
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.with", true));
        labelList.add(new GenericPair<Object, Boolean>(", ", false));
        
        labelList.add(new GenericPair<Object, Boolean>("label.in", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.minimum", true));
        labelList.add(new GenericPair<Object, Boolean>(", ", false));
        
        labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.credits", true));
        }
        
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }
        return labelList;
    }

    @Override
    public RuleResult evaluate(final EnrolmentContext enrolmentContext) {
        // TODO Auto-generated method stub
        return null;
    }

    @Deprecated
    public Double getMinimum() {
        // TODO Auto-generated method stub
        return super.getMinimumCredits();
    }

    @Deprecated
    public void setMinimum(Double minimum) {
        // TODO Auto-generated method stub
        super.setMinimumCredits(minimum);
    }
    
}
