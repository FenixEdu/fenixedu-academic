package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;

public class DegreeTeachingServiceCorrection extends DegreeTeachingServiceCorrection_Base {

    public DegreeTeachingServiceCorrection(TeacherService teacherService, Professorship professorship, BigDecimal correction,
            String reason, ExecutionSemester correctedExecutionSemester) {
        super();
        if (teacherService == null || professorship == null || correction == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacherService(teacherService);
        setProfessorship(professorship);
        setCorrection(correction);
        setReason(reason);
        String correctionString = getCorrection().toString();
        if (getCorrection().compareTo(BigDecimal.ZERO) >= 0) {
            correctionString = "+" + correctionString;
        }
        setCorrectedExecutionSemester(correctedExecutionSemester != null ? correctedExecutionSemester : teacherService
                .getExecutionPeriod());
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.schedule.correction", getProfessorship()
                        .getExecutionCourse().getName(), getProfessorship().getExecutionCourse().getDegreePresentationString(),
                getReason(), correctionString));
    }

    @Override
    public Double getCredits() {
        return getProfessorship().getExecutionCourse().getUnitCreditValue().multiply(getCorrection()).doubleValue();
    }

}
