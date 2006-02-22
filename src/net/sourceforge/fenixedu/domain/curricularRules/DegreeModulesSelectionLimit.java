package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeModulesSelectionLimit extends DegreeModulesSelectionLimit_Base {

    /**
     * This constructor should be used in context of Composite Rule
     */
    protected DegreeModulesSelectionLimit(Integer minimum, Integer maximum) {
        super();
        checkLimits(minimum, maximum);
        setMinimum(minimum);
        setMaximum(maximum);
        setCurricularRuleType(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT);
    }

    protected DegreeModulesSelectionLimit(DegreeModule degreeModuleToApplyRule,
            CourseGroup contextCourseGroup, ExecutionPeriod begin, ExecutionPeriod end, Integer minimum,
            Integer maximum) {

        this(minimum, maximum);

        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
    }
    
    protected void edit(CourseGroup contextCourseGroup, Integer minimumLimit, Integer maximumLimit) {
        checkLimits(minimumLimit, maximumLimit);
        setContextCourseGroup(contextCourseGroup);
        setMinimum(minimumLimit);
        setMaximum(maximumLimit);
    }
    
    private void checkLimits(Integer minimum, Integer maximum) throws DomainException {
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.intValue() > maximum.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.modulesSelection", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        if (getMinimum().intValue() == getMaximum().intValue()) {
            labelList.add(new GenericPair<Object, Boolean>("label.choose", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            if (getMinimum().intValue() == 1) {
                labelList.add(new GenericPair<Object, Boolean>("label.module", true));
            } else {
                labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
            }
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.chooseFrom", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.to", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMaximum(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
        }
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getName(), false));
        }
        return labelList;
    }

    @Override
    public boolean evaluate(Class<? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters 
    }

}
