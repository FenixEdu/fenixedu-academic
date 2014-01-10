package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.OptionalDismissalLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;

public class OptionalDismissal extends OptionalDismissal_Base {

    public OptionalDismissal() {
        super();
    }

    public OptionalDismissal(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse,
            Double ectsCredits) {
        init(credits, curriculumGroup, optionalCurricularCourse, ectsCredits);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void init(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse,
            Double ectsCredits) {
        init(credits, curriculumGroup, optionalCurricularCourse);
        checkCredits(ectsCredits);
        setEctsCredits(ectsCredits);
    }

    private void checkCredits(final Double ectsCredits) {
        if (ectsCredits == null || ectsCredits.doubleValue() == 0) {
            throw new DomainException("error.OptionalDismissal.invalid.credits");
        }
    }

    @Override
    public OptionalCurricularCourse getCurricularCourse() {
        return (OptionalCurricularCourse) super.getCurricularCourse();
    }

    @Override
    public void setDegreeModule(final DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof OptionalCurricularCourse)) {
            throw new DomainException("error.optionalDismissal.DegreeModuleCanOnlyBeOptionalCurricularCourse");
        }
        super.setDegreeModule(degreeModule);
    }

    @Override
    public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[OD ").append(hasDegreeModule() ? getDegreeModule().getName() : "").append(" ]\n");
        return builder;
    }

    @Override
    public boolean isSimilar(Dismissal dismissal) {
        return dismissal instanceof OptionalDismissal && super.isSimilar(dismissal)
                && hasSameEctsCredits((OptionalDismissal) dismissal);
    }

    private boolean hasSameEctsCredits(OptionalDismissal dismissal) {
        return getEctsCredits().equals(dismissal.getEctsCredits());
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new OptionalDismissalLog(action, getRegistration(), getCurricularCourse(), getCredits(), getEctsCredits(),
                getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

}
