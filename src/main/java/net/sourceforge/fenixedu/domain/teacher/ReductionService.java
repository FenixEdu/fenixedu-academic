package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentCreditsPool;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessYear;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;

public class ReductionService extends ReductionService_Base {

    public ReductionService(final TeacherService teacherService, final BigDecimal creditsReduction) {
        super();
        setRootDomainObject(Bennu.getInstance());
        if (teacherService == null) {
            throw new DomainException("arguments can't be null");
        }
        if (teacherService.getReductionService() != null) {
            throw new DomainException("error.already.requested.reduction");
        }
        setTeacherService(teacherService);
        setCreditsReduction(creditsReduction);
        log("label.teacher.schedule.reductionService.create", getCreditsReduction());
    }

    public ReductionService(final TeacherService teacherService, final Boolean requestCreditsReduction) {
        super();
        setRootDomainObject(Bennu.getInstance());
        if (teacherService == null) {
            throw new DomainException("arguments can't be null");
        }
        if (teacherService.getReductionService() != null) {
            throw new DomainException("error.already.requested.reduction");
        }
        setTeacherService(teacherService);
        setRequestCreditsReduction(requestCreditsReduction);
        log("label.teacher.schedule.reductionService.create", getRequestCreditsReduction());
    }

    public ReductionService(final BigDecimal creditsReductionAttributed, final TeacherService teacherService) {
        super();
        setRootDomainObject(Bennu.getInstance());
        if (teacherService == null) {
            throw new DomainException("arguments can't be null");
        }
        if (teacherService.getReductionService() != null) {
            throw new DomainException("error.already.requested.reduction");
        }
        setTeacherService(teacherService);
        setCreditsReductionAttributed(creditsReductionAttributed);
        log("label.teacher.schedule.reductionService.approve", getCreditsReductionAttributed());
    }

    @Override
    public void setCreditsReduction(BigDecimal creditsReduction) {
        checkCredits(creditsReduction);
        BigDecimal maxCreditsFromEvaluationAndAge = getMaxCreditsFromEvaluationAndAge();
        if (creditsReduction.compareTo(maxCreditsFromEvaluationAndAge) > 0) {
            throw new DomainException("label.creditsReduction.exceededMaxAllowed.evaluationAndAge",
                    maxCreditsFromEvaluationAndAge.toString());
        }
        super.setCreditsReduction(creditsReduction);
        Department lastWorkingDepartment = getDepartment();
        setPendingApprovalFromDepartment(lastWorkingDepartment);
        log("label.teacher.schedule.reductionService.edit", getCreditsReduction());
    }

    private Department getDepartment() {
        return getTeacherService().getTeacher().getLastWorkingDepartment(
                getTeacherService().getExecutionPeriod().getBeginDateYearMonthDay(),
                getTeacherService().getExecutionPeriod().getEndDateYearMonthDay());
    }

    public BigDecimal getMaxCreditsFromEvaluationAndAge() {
        BigDecimal maxCreditsFromEvaluation = getTeacherEvaluationMark();
        BigDecimal maxCreditsFromAge = getTeacherMaxCreditsFromAge();
        BigDecimal maxCreditsFromEvaluationAndAge = maxCreditsFromEvaluation.add(maxCreditsFromAge);
        BigDecimal maxCreditsReduction = getMaxCreditsReduction();
        return maxCreditsReduction.min(maxCreditsFromEvaluationAndAge);
    }

    @Override
    public void setRequestCreditsReduction(Boolean requestCreditsReduction) {
        checkTeacherCategory();
        super.setRequestCreditsReduction(requestCreditsReduction);
        Department lastWorkingDepartment = requestCreditsReduction ? getDepartment() : null;
        setPendingApprovalFromDepartment(lastWorkingDepartment);
        log("label.teacher.schedule.reductionService.edit", getRequestCreditsReduction());
    }

    @Override
    public void setCreditsReductionAttributed(BigDecimal creditsReductionAttributed) {
        checkCredits(creditsReductionAttributed);
        super.setCreditsReductionAttributed(creditsReductionAttributed);
        setAttributionDate(new DateTime());
        setPendingApprovalFromDepartment(null);
        log("label.teacher.schedule.reductionService.approve", getCreditsReductionAttributed());
    }

    private void checkCredits(BigDecimal creditsReduction) {
        if (creditsReduction == null) {
            creditsReduction = BigDecimal.ZERO;
        }
        checkTeacherCategory();
        BigDecimal maxCreditsReduction = getMaxCreditsReduction();
        if (creditsReduction.compareTo(maxCreditsReduction) > 0) {
            throw new DomainException("label.creditsReduction.exceededMaxAllowed", maxCreditsReduction.toString());
        }
    }

    private void checkTeacherCategory() {
        if (!getTeacherService().getTeacher().isTeacherProfessorCategory(getTeacherService().getExecutionPeriod())) {
            throw new DomainException("label.creditsReduction.invalidCategory");
        }
    }

    private BigDecimal getTeacherMaxCreditsFromAge() {
        YearMonthDay dateOfBirthYearMonthDay = getTeacherService().getTeacher().getPerson().getDateOfBirthYearMonthDay();
        if (dateOfBirthYearMonthDay != null) {
            Interval interval =
                    new Interval(dateOfBirthYearMonthDay.toLocalDate().toDateTimeAtStartOfDay(), getTeacherService()
                            .getExecutionPeriod().getEndDateYearMonthDay().plusDays(1).toLocalDate().toDateTimeAtStartOfDay());
            int age = interval.toPeriod(PeriodType.years()).getYears();
            if (age >= 65) {
                return BigDecimal.ONE;
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getTeacherEvaluationMark() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = null;
        for (final FacultyEvaluationProcessYear facultyEvaluationProcessYear : Bennu.getInstance()
                .getFacultyEvaluationProcessYearSet()) {
            if (facultyEvaluationProcessYear.getApprovedTeacherEvaluationProcessMarkSet().size() != 0
                    && (lastFacultyEvaluationProcessYear == null || facultyEvaluationProcessYear.getYear().compareTo(
                            lastFacultyEvaluationProcessYear.getYear()) > 0)) {
                lastFacultyEvaluationProcessYear = facultyEvaluationProcessYear;
            }
        }
        TeacherEvaluationProcess lastTeacherEvaluationProcess = null;
        for (TeacherEvaluationProcess teacherEvaluationProcess : getTeacherService().getTeacher().getPerson()
                .getTeacherEvaluationProcessFromEvalueeSet()) {
            if (teacherEvaluationProcess.getFacultyEvaluationProcess().equals(
                    lastFacultyEvaluationProcessYear.getFacultyEvaluationProcess())) {
                lastTeacherEvaluationProcess = teacherEvaluationProcess;
                break;
            }
        }
        TeacherEvaluationMark approvedEvaluationMark = null;

        BigDecimal maxCreditsReduction = getMaxCreditsReduction();
        if (lastTeacherEvaluationProcess != null) {
            for (ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : lastTeacherEvaluationProcess
                    .getApprovedTeacherEvaluationProcessMarkSet()) {
                if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear().equals(
                        lastFacultyEvaluationProcessYear)) {
                    approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
                    if (approvedEvaluationMark != null) {
                        switch (approvedEvaluationMark) {
                        case EXCELLENT:
                            return maxCreditsReduction;
                        case VERY_GOOD:
                            return BigDecimal.ZERO.max(maxCreditsReduction.subtract(BigDecimal.ONE));
                        case GOOD:
                            return BigDecimal.ZERO.max(maxCreditsReduction.subtract(new BigDecimal(2)));
                        default:
                            return BigDecimal.ZERO;
                        }
                    } else {
                        return BigDecimal.ZERO;
                    }
                }
            }
        } else {
            return maxCreditsReduction;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getMaxCreditsReduction() {
        Department department = getDepartment();
        DepartmentCreditsPool departmentCreditsPool =
                DepartmentCreditsPool.getDepartmentCreditsPool(department, getTeacherService().getExecutionPeriod()
                        .getExecutionYear());

        return departmentCreditsPool == null || departmentCreditsPool.getMaximumCreditsReduction() == null ? new BigDecimal(3) : departmentCreditsPool
                .getMaximumCreditsReduction();
    }

    private void log(final String key, BigDecimal credits) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", key));
        log.append(credits);
        new TeacherServiceLog(getTeacherService(), log.toString());
    }

    private void log(final String key, Boolean requested) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", key));
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                requested ? "message.yes" : "message.no"));
        new TeacherServiceLog(getTeacherService(), log.toString());
    }

    @Deprecated
    public boolean hasCreditsReductionAttributed() {
        return getCreditsReductionAttributed() != null;
    }

    @Deprecated
    public boolean hasAttributionDate() {
        return getAttributionDate() != null;
    }

    @Deprecated
    public boolean hasCreditsReduction() {
        return getCreditsReduction() != null;
    }

}
