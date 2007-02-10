package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class RestrictionBetweenDegreeModules extends RestrictionBetweenDegreeModules_Base {

    private RestrictionBetweenDegreeModules(final CourseGroup precedenceDegreeModule, final Double minimumCredits) {
        super();
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimumCredits(minimumCredits);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_BETWEEN_DEGREE_MODULES);
    }

    private void checkParameters(final DegreeModule precedenceDegreeModule, final Double minimumCredits) throws DomainException {
        if (precedenceDegreeModule == null || minimumCredits == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }
    
    protected RestrictionBetweenDegreeModules(final CourseGroup degreeModuleToApplyRule, final CourseGroup precedenceDegreeModule,
            final Double minimumCredits, final CourseGroup contextCourseGroup, final ExecutionPeriod begin, final ExecutionPeriod end) {
        
        this(precedenceDegreeModule, minimumCredits);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }
    
    protected void edit(final CourseGroup precedenceDegreeModule, final Double minimumCredits, final CourseGroup contextCourseGroup) {
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimumCredits(minimumCredits);
        setContextCourseGroup(contextCourseGroup);
    }
    
    @Override
    public CourseGroup getDegreeModuleToApplyRule() {
        return (CourseGroup) super.getDegreeModuleToApplyRule();
    }
    
    @Override
    public CourseGroup getPrecedenceDegreeModule() {
        return (CourseGroup) super.getPrecedenceDegreeModule();
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        
        if (belongsToCompositeRule() && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
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
        
        if (getMinimumCredits().doubleValue() != 0.0) {
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.with", true));
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.minimum", true));
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            
            labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
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
    
    public boolean hasMinimumCredits() {
	return getMinimumCredits() != null && getMinimumCredits().doubleValue() != 0.0;
    }
    
    public boolean allowCredits(final Double credits) {
	return credits.doubleValue() >= getMinimumCredits().doubleValue() ;
    }
}
