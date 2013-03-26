package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.joda.time.YearMonthDay;

public class PhdRegistrationConclusionBean extends RegistrationConclusionBean {

    static private final long serialVersionUID = 1L;

    public PhdRegistrationConclusionBean(Registration registration) {
        super(registration);
        setCycleCurriculumGroup(registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE));
    }

    public RegistrationStateType getActiveStateType() {
        return getRegistration().getActiveStateType();
    }

    public YearMonthDay getStartDate() {
        return getRegistration().getStartDate();
    }

    public String getDegreeNameWithDescription() {
        return getRegistration().getDegreeNameWithDescription();
    }

    @Override
    public YearMonthDay getConclusionDate() {
        return isConclusionProcessed() ? getCycleCurriculumGroup().getConclusionDate() : null;
    }

    @Override
    public Integer getFinalAverage() {
        return isConclusionProcessed() ? getCycleCurriculumGroup().getFinalAverage() : null;
    }

}
