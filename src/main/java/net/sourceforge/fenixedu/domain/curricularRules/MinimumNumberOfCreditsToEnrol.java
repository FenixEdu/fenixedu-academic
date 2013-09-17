package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class MinimumNumberOfCreditsToEnrol extends MinimumNumberOfCreditsToEnrol_Base {

    private MinimumNumberOfCreditsToEnrol(final Double minimumNumberOfCredits) {
        super();
        checkCredits(minimumNumberOfCredits);
        setMinimumCredits(minimumNumberOfCredits);
        setCurricularRuleType(CurricularRuleType.MINIMUM_NUMBER_OF_CREDITS_TO_ENROL);
    }

    private void checkCredits(final Double minimumNumberOfCredits) throws DomainException {
        if (minimumNumberOfCredits == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    protected MinimumNumberOfCreditsToEnrol(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end, final Double minimumNumberOfCredits) {

        this(minimumNumberOfCredits);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(final CourseGroup contextCourseGroup, final Double minimumNumberOfCredits) {
        checkCredits(minimumNumberOfCredits);
        setContextCourseGroup(contextCourseGroup);
        setMinimumCredits(minimumNumberOfCredits);
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.minimumNumberOfCreditsToEnrol", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }

        return labelList;
    }

    public boolean allowCredits(final Double credits) {
        return credits.doubleValue() >= getMinimumCredits().doubleValue();
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }
    @Deprecated
    public boolean hasMinimumCredits() {
        return getMinimumCredits() != null;
    }

}
