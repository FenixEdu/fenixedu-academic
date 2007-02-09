package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class RestrictionDoneDegreeModule extends RestrictionDoneDegreeModule_Base {
       
    private RestrictionDoneDegreeModule(final DegreeModule done) {
        super();
        if (done == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }        
        setPrecedenceDegreeModule(done);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE);
    }

    public RestrictionDoneDegreeModule(CurricularCourse toApply, CurricularCourse done,
            CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO, 
            ExecutionPeriod begin, ExecutionPeriod end) {
        
        this(done);
        init(toApply, contextCourseGroup, begin, end);
        if (curricularPeriodInfoDTO != null) {
            setCurricularPeriodType(curricularPeriodInfoDTO.getPeriodType());
            setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
        }
    }
    
    @Override
    public CurricularCourse getDegreeModuleToApplyRule() {
	return (CurricularCourse) super.getDegreeModuleToApplyRule();
    }

    @Override
    public CurricularCourse getPrecedenceDegreeModule() {
	return (CurricularCourse) super.getPrecedenceDegreeModule();
    }

    protected void edit(DegreeModule done, CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO) {
        setPrecedenceDegreeModule(done);
        setContextCourseGroup(contextCourseGroup);
        setCurricularPeriodType(curricularPeriodInfoDTO.getPeriodType());
        setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        
        if (belongsToCompositeRule()
                && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceNotDone", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceDone", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(": ", false));

        // getting full name only for course groups
        String precedenceDegreeModule = (getPrecedenceDegreeModule().isLeaf()) ? getPrecedenceDegreeModule().getName() : getPrecedenceDegreeModule().getOneFullName();
        labelList.add(new GenericPair<Object, Boolean>(precedenceDegreeModule, false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }

        if (!hasNoCurricularPeriodOrder()) {
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

}
