package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CreditsLimit extends CreditsLimit_Base {

    private CreditsLimit(Double minimum, Double maximum) {
        super();
        checkCredits(minimum, maximum);
        setMinimum(minimum);
        setMaximum(maximum);
        setCurricularRuleType(CurricularRuleType.CREDITS_LIMIT);
    }

    protected CreditsLimit(DegreeModule degreeModuleToApplyRule, CourseGroup contextCourseGroup,
            ExecutionPeriod begin, ExecutionPeriod end, Double minimum, Double maximum) {

        this(minimum, maximum);

        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        
        checkExecutionPeriods(begin, end);

        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
    }
    
    protected void edit(CourseGroup contextCourseGroup, Double minimumCredits, Double maximumCredits) {
        checkCredits(minimumCredits, maximumCredits);
        setContextCourseGroup(contextCourseGroup);
        setMinimum(minimumCredits);
        setMaximum(maximumCredits);
    }
    
    private void checkCredits(Double minimum, Double maximum) throws DomainException {
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.doubleValue() > maximum.doubleValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.creditsForApproval", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        if (getMinimum().doubleValue() == getMaximum().doubleValue()) {
            labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
        } else {
            labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.to", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMaximum(), false));
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
        // no domain parameters
    }

    @Deprecated
    public Double getMaximum() {
        return super.getMaximumCredits();
    }

    @Deprecated
    public Double getMinimum() {
        return super.getMinimumCredits();
    }

    @Deprecated
    public void setMaximum(Double maximum) {
        super.setMaximumCredits(maximum);
    }

    @Deprecated
    public void setMinimum(Double minimum) {
        super.setMinimumCredits(minimum);
    }

    
}
