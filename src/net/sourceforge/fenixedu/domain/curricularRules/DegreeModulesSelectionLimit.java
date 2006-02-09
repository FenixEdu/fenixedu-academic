package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeModulesSelectionLimit extends DegreeModulesSelectionLimit_Base {

    /**
     * This constructor should be used in context of Composite Rule
     */
    protected DegreeModulesSelectionLimit(Integer minimum, Integer maximum) {
        super();
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.intValue() > maximum.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
        setMinimum(minimum);
        setMaximum(maximum);
        setCurricularRuleType(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT);
    }

    public DegreeModulesSelectionLimit(CourseGroup degreeModuleToApplyRule,
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
    
    public void edit(CourseGroup contextCourseGroup, Integer minimumLimit, Integer maximumLimit) {
        setContextCourseGroup(contextCourseGroup);
        setMinimum(minimumLimit);
        setMaximum(maximumLimit);
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.modulesSelection", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.chooseFrom", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>(getMinimum(), false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.to", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>(getMaximum(), false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.modules", true));

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

}
