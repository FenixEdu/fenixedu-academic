package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessYear;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class ReductionServiceBean implements Serializable {
    private Teacher teacher;
    private ReductionService reductionService;

    public ReductionServiceBean(Teacher teacher) {
        this.teacher = teacher;
    }

    public ReductionServiceBean(ReductionService reductionService) {
        this.reductionService = reductionService;
        this.teacher = reductionService.getTeacherService().getTeacher();
    }

    public ReductionServiceBean() {
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ReductionService getReductionService() {
        return reductionService;
    }

    public void setReductionService(ReductionService reductionService) {
        this.reductionService = reductionService;
    }

    public String getTeacherCategory() {
        ProfessionalCategory category = getTeacher().getCategoryByPeriod(ExecutionSemester.readActualExecutionSemester());
        return category != null ? category.getName().getContent() : null;
    }

    @Atomic
    public TeacherService getTeacherService() {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }
        return teacherService;
    }

    public FacultyEvaluationProcessYear getFacultyEvaluationProcessYear() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = null;
        for (final FacultyEvaluationProcessYear facultyEvaluationProcessYear : Bennu.getInstance()
                .getFacultyEvaluationProcessYearSet()) {
            if (facultyEvaluationProcessYear.getApprovedTeacherEvaluationProcessMarkSet().size() != 0
                    && (lastFacultyEvaluationProcessYear == null || facultyEvaluationProcessYear.getYear().compareTo(
                            lastFacultyEvaluationProcessYear.getYear()) > 0)) {
                lastFacultyEvaluationProcessYear = facultyEvaluationProcessYear;
            }
        }
        return lastFacultyEvaluationProcessYear;
    }

    public String getTeacherEvaluationMarkString() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = getFacultyEvaluationProcessYear();
        TeacherEvaluationProcess lastTeacherEvaluationProcess = null;
        for (TeacherEvaluationProcess teacherEvaluationProcess : getTeacher().getPerson()
                .getTeacherEvaluationProcessFromEvalueeSet()) {
            if (teacherEvaluationProcess.getFacultyEvaluationProcess().equals(
                    lastFacultyEvaluationProcessYear.getFacultyEvaluationProcess())) {
                lastTeacherEvaluationProcess = teacherEvaluationProcess;
                break;
            }
        }
        TeacherEvaluationMark approvedEvaluationMark = null;
        if (lastTeacherEvaluationProcess != null) {
            for (ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : lastTeacherEvaluationProcess
                    .getApprovedTeacherEvaluationProcessMarkSet()) {
                if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear().equals(
                        lastFacultyEvaluationProcessYear)) {
                    approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
                    if (approvedEvaluationMark != null) {
                        return BundleUtil.getEnumName(approvedEvaluationMark);
                    } else {
                        return "N/A";
                    }
                }
            }
        }
        return null;
    }
}
