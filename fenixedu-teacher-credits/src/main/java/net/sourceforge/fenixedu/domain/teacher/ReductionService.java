/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.teacher;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.credits.util.ReductionServiceBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark;
import org.fenixedu.academic.domain.teacher.evaluation.FacultyEvaluationProcessYear;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationMark;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationProcess;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

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
        Department lastDepartment = getDepartment();
        setPendingApprovalFromDepartment(lastDepartment);
        log("label.teacher.schedule.reductionService.edit", getCreditsReduction());
    }

    private Department getDepartment() {
        return getTeacherService().getTeacher().getLastDepartment(getTeacherService().getExecutionPeriod().getAcademicInterval());
    }

    @Override
    public void setRequestCreditsReduction(Boolean requestCreditsReduction) {
        checkTeacherCategory();
        super.setRequestCreditsReduction(requestCreditsReduction);
        Department lastDepartment = requestCreditsReduction ? getDepartment() : null;
        setPendingApprovalFromDepartment(lastDepartment);
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
        if (!ProfessionalCategory.isTeacherProfessorCategory(getTeacherService().getTeacher(), getTeacherService().getExecutionPeriod())) {
            throw new DomainException("label.creditsReduction.invalidCategory");
        }
    }

    public BigDecimal getMaxCreditsFromEvaluationAndAge() {
        ReductionServiceBean reductionServiceBean = new ReductionServiceBean(this);
        return reductionServiceBean.getMaxCreditsFromEvaluationAndAge();
    }

    private BigDecimal getMaxCreditsReduction() {
        ReductionServiceBean reductionServiceBean = new ReductionServiceBean(this);
        return reductionServiceBean.getMaxCreditsReduction();
    }

    private void log(final String key, BigDecimal credits) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, key));
        log.append(credits);
        new TeacherServiceLog(getTeacherService(), log.toString());
    }

    private void log(final String key, Boolean requested) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, key));
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, requested ? "message.yes" : "message.no"));
        new TeacherServiceLog(getTeacherService(), log.toString());
    }

}
