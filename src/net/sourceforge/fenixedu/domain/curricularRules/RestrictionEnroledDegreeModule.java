package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class RestrictionEnroledDegreeModule extends RestrictionEnroledDegreeModule_Base {

    /**
     * This constructor should be used in context of Composite Rule
     */
    protected RestrictionEnroledDegreeModule(DegreeModule enroledDegreeModule) {
        super();
        if (enroledDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setPrecedenceDegreeModule(enroledDegreeModule);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE);
    }

    protected RestrictionEnroledDegreeModule(CurricularCourse degreeModuleToApplyRule,
            DegreeModule enroledDegreeModule, CourseGroup contextCourseGroup,
            CurricularPeriodInfoDTO curricularPeriodInfoDTO, ExecutionPeriod begin, ExecutionPeriod end) {

        this(enroledDegreeModule);

        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setContextCourseGroup(contextCourseGroup);
        setCurricularPeriodType(curricularPeriodInfoDTO.getPeriodType());
        setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
    }
    
    protected void edit(DegreeModule enroledDegreeModule, CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO) {
        setPrecedenceDegreeModule(enroledDegreeModule);
        setContextCourseGroup(contextCourseGroup);
        setCurricularPeriodType(curricularPeriodInfoDTO.getPeriodType());
        setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        if (belongsToCompositeRule()
                && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceNotEnrolled", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceEnrolled", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        labelList.add(new GenericPair<Object, Boolean>(getPrecedenceDegreeModule().getName(), false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getName(), false));
        }

        if (!getCurricularPeriodOrder().equals(0)) {
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.and", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodType().name(), true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
        }

        return labelList;
    }

    @Override
    public boolean evaluate(Class<? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }

}
