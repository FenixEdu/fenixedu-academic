package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class OtherService extends OtherService_Base {

    public OtherService(TeacherService teacherService, Double credits, String reason, ExecutionSemester correctedExecutionSemester) {
        super();
        setValues(teacherService, credits, reason, correctedExecutionSemester);
    }

    public OtherService() {
        super();
    }

    public OtherService(TeacherService teacherService, Double credits, String reason) {
        setValues(teacherService, credits, reason, null);
    }

    public void setValues(TeacherService teacherService, Double credits, String reason,
            ExecutionSemester correctedExecutionSemester) {
        if (teacherService == null || credits == null || reason == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacherService(teacherService);
        setCredits(credits);
        setReason(reason);
        setCorrectedExecutionSemester(correctedExecutionSemester != null ? correctedExecutionSemester : teacherService
                .getExecutionPeriod());
        new TeacherServiceLog(teacherService, BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.teacher.otherService", credits.toString(), reason, getCorrectedExecutionSemester().getExecutionYear()
                        .getQualifiedName()));
    }

    @Override
    @Service
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.otherService.delete", getCredits().toString(),
                getReason(), getCorrectedExecutionSemester().getExecutionYear().getQualifiedName()));
        setTeacherService(null);
        setCorrectedExecutionSemester(null);
        super.delete();
    }

    @Override
    public Double getCredits() {
        return round(super.getCredits());
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }
}
