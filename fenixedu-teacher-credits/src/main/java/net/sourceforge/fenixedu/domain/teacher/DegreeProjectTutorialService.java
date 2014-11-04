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
import java.util.List;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.credits.util.ProjectTutorialServiceBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class DegreeProjectTutorialService extends DegreeProjectTutorialService_Base {

    public DegreeProjectTutorialService(Professorship professorship, Attends attend, Integer percentageValue) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setProfessorship(professorship);
        TeacherService teacherService =
                TeacherService.getTeacherService(professorship.getTeacher(), professorship.getExecutionCourse()
                        .getExecutionYear().getNextExecutionYear().getFirstExecutionPeriod());
        setTeacherService(teacherService);
        setAttend(attend);
        setPercentageValue(percentageValue);
    }

    @Override
    public void setPercentageValue(Integer percentageValue) {
        checkPercentage(percentageValue);
        super.setPercentageValue(percentageValue);
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.degreeProjectTutorialService.change", getProfessorship().getExecutionCourse().getName(),
                getProfessorship().getTeacher().getPerson().getNickname(), getAttend().getRegistration().getNumber().toString(),
                getPercentageValue().toString()));
    }

    private void checkPercentage(Integer percentageValue) {
        Integer availablePercentage = new Integer(100);
        if (percentageValue == null || percentageValue.compareTo(0) < 0 || percentageValue.compareTo(availablePercentage) > 0) {
            throw new DomainException("message.invalid.percentage");
        }
        for (DegreeProjectTutorialService degreeProjectTutorialService : getAttend().getDegreeProjectTutorialServicesSet()) {
            if (!degreeProjectTutorialService.equals(this)) {
                availablePercentage = availablePercentage - degreeProjectTutorialService.getPercentageValue();
            }
        }
        if (percentageValue.compareTo(availablePercentage) > 0) {
            throw new DomainException("message.exceeded.percentage");
        }
    }

    @Atomic
    public static void updateProjectTutorialService(List<ProjectTutorialServiceBean> projectTutorialServicesBean) {
        for (ProjectTutorialServiceBean projectTutorialServiceBean : projectTutorialServicesBean) {
            if (!projectTutorialServiceBean.getProfessorship().getExecutionCourse().getProjectTutorialCourse()) {
                throw new DomainException("message.invalid.executionCourseType");
            }
            DegreeProjectTutorialService degreeProjectTutorialService =
                    projectTutorialServiceBean.getDegreeProjectTutorialService();
            if (projectTutorialServiceBean.getPercentage() == null) {
                projectTutorialServiceBean.setPercentage(0);
            }
            if (degreeProjectTutorialService == null && projectTutorialServiceBean.getPercentage() != 0) {
                degreeProjectTutorialService =
                        new DegreeProjectTutorialService(projectTutorialServiceBean.getProfessorship(),
                                projectTutorialServiceBean.getAttend(), projectTutorialServiceBean.getPercentage());

            } else if (degreeProjectTutorialService != null
                    && !degreeProjectTutorialService.getPercentageValue().equals(projectTutorialServiceBean.getPercentage())) {
                degreeProjectTutorialService.setPercentageValue(projectTutorialServiceBean.getPercentage());
            }
        }
    }

    @Override
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.degreeProjectTutorialService.delete", getProfessorship().getExecutionCourse().getName(),
                getProfessorship().getTeacher().getPerson().getNickname(), getAttend().getRegistration().getNumber().toString(),
                getPercentageValue().toString()));
        setAttend(null);
        setProfessorship(null);
        super.delete();
    }

    public BigDecimal getDegreeProjectTutorialServiceCredits() {
        if (getAttend().getEnrolment() != null && getAttend().getEnrolment().isApproved()) {
            return new BigDecimal((((double) getPercentageValue()) / 100)
                    * (getAttend().getExecutionCourse().getEctsCredits() / 60));
        }
        return BigDecimal.ZERO;
    }

}
