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

import org.fenixedu.academic.dto.department.TeacherPersonalExpectationBean;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherAutoEvaluationDefinitionPeriod;
import org.fenixedu.academic.domain.TeacherExpectationDefinitionPeriod;
import org.fenixedu.academic.domain.TeacherPersonalExpectationPeriod;
import org.fenixedu.academic.domain.TeacherPersonalExpectationsEvaluationPeriod;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class TeacherPersonalExpectation extends TeacherPersonalExpectation_Base {

    private TeacherPersonalExpectation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherPersonalExpectation(TeacherPersonalExpectationBean infoTeacherPersonalExpectation) {
        this();
        ExecutionYear executionYear = infoTeacherPersonalExpectation.getExecutionYear();
        Teacher teacher = infoTeacherPersonalExpectation.getTeacher();

        if (executionYear != null && teacher != null) {
            if (getTeacherPersonalExpectationByExecutionYear(teacher, executionYear) != null) {
                throw new DomainException("error.exception.personalExpectation.already.exists");
            }
        }

        setExecutionYear(executionYear);
        setTeacher(teacher);

        if (!isAllowedToEditExpectation()) {
            throw new DomainException("error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
        }

        setProperties(infoTeacherPersonalExpectation);
    }

    @Override
    public void setTutorComment(String tutorComment) {
        if (isAllowedToEditEvaluation()) {
            super.setTutorComment(tutorComment);
        } else {
            throw new DomainException("error.exception.personalExpectation.evaluationPeriodForExecutionYearAlreadyExpired");
        }
    }

    @Override
    public void setTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new DomainException("error.TeacherPersonalExpectation.empty.teacher");
        }
        super.setTeacher(teacher);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.TeacherPersonalExpectation.empty.executionYear");
        }
        super.setExecutionYear(executionYear);
    }

    @Override
    public void setAutoEvaluation(String autoEvaluation) {
        if (isAllowedToEditAutoEvaluation()) {
            super.setAutoEvaluation(autoEvaluation);
        } else {
            throw new DomainException("error.label.notAbleToEditAutoEvaluation");
        }
    }

    public String getUtlOrgans() {
        return getUniversityOrgans();
    }

    public void setUtlOrgans(String utlOrgans) {
        setUniversityOrgans(utlOrgans);
    }

    public String getIstOrgans() {
        return getInstitutionOrgans();
    }

    public void setIstOrgans(String istOrgans) {
        setInstitutionOrgans(istOrgans);
    }

    public boolean isAllowedToEditExpectation() {
        Department department = getTeacher().getDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    TeacherPersonalExpectationPeriod.getTeacherExpectationDefinitionPeriodForExecutionYear(department,
                            getExecutionYear());
            return (period == null) ? false : period.isPeriodOpen();
        }
        return false;
    }

    public boolean isAllowedToEditAutoEvaluation() {
        Department department = getTeacher().getDepartment();
        if (department != null) {
            TeacherAutoEvaluationDefinitionPeriod period =
                    TeacherPersonalExpectationPeriod.getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(department,
                            getExecutionYear());
            return (period == null) ? false : period.isPeriodOpen();
        }
        return false;
    }

    public boolean isAllowedToEditEvaluation() {
        Department department = getTeacher().getDepartment();
        if (department != null) {
            TeacherPersonalExpectationsEvaluationPeriod period =
                    TeacherPersonalExpectationPeriod.getTeacherPersonalExpectationsEvaluationPeriodByExecutionYear(department,
                            getExecutionYear());
            return (period == null) ? false : period.isPeriodOpen();
        }
        return false;
    }

    private void setProperties(TeacherPersonalExpectationBean infoTeacherPersonalExpectation) {
        setEducationMainFocus(infoTeacherPersonalExpectation.getEducationMainFocus());
        setGraduations(infoTeacherPersonalExpectation.getGraduations());
        setGraduationsDescription(infoTeacherPersonalExpectation.getGraduationsDescription());
        setCientificPosGraduations(infoTeacherPersonalExpectation.getCientificPosGraduations());
        setCientificPosGraduationsDescription(infoTeacherPersonalExpectation.getCientificPosGraduationsDescription());
        setProfessionalPosGraduations(infoTeacherPersonalExpectation.getProfessionalPosGraduations());
        setProfessionalPosGraduationsDescription(infoTeacherPersonalExpectation.getProfessionalPosGraduationsDescription());
        setSeminaries(infoTeacherPersonalExpectation.getSeminaries());
        setSeminariesDescription(infoTeacherPersonalExpectation.getSeminariesDescription());
        setResearchAndDevProjects(infoTeacherPersonalExpectation.getResearchAndDevProjects());
        setJornalArticlePublications(infoTeacherPersonalExpectation.getJornalArticlePublications());
        setBookPublications(infoTeacherPersonalExpectation.getBookPublications());
        setConferencePublications(infoTeacherPersonalExpectation.getConferencePublications());
        setTechnicalReportPublications(infoTeacherPersonalExpectation.getTechnicalReportPublications());
        setPatentPublications(infoTeacherPersonalExpectation.getPatentPublications());
        setOtherPublications(infoTeacherPersonalExpectation.getOtherPublications());
        setOtherPublicationsDescription(infoTeacherPersonalExpectation.getOtherPublicationsDescription());
        setResearchAndDevMainFocus(infoTeacherPersonalExpectation.getResearchAndDevMainFocus());
        setPhdOrientations(infoTeacherPersonalExpectation.getPhdOrientations());
        setMasterDegreeOrientations(infoTeacherPersonalExpectation.getMasterDegreeOrientations());
        setFinalDegreeWorkOrientations(infoTeacherPersonalExpectation.getFinalDegreeWorkOrientations());
        setOrientationsMainFocus(infoTeacherPersonalExpectation.getOrientationsMainFocus());
        setUniversityServiceMainFocus(infoTeacherPersonalExpectation.getUniversityServiceMainFocus());
        setDepartmentOrgans(infoTeacherPersonalExpectation.getDepartmentOrgans());
        setIstOrgans(infoTeacherPersonalExpectation.getInstitutionOrgans());
        setUtlOrgans(infoTeacherPersonalExpectation.getUniversityOrgans());
        setProfessionalActivityMainFocus(infoTeacherPersonalExpectation.getProfessionalActivityMainFocus());
        setCientificComunityService(infoTeacherPersonalExpectation.getCientificComunityService());
        setSocietyService(infoTeacherPersonalExpectation.getSocietyService());
        setConsulting(infoTeacherPersonalExpectation.getConsulting());
        setCompanySocialOrgans(infoTeacherPersonalExpectation.getCompanySocialOrgans());
        setCompanyPositions(infoTeacherPersonalExpectation.getCompanyPositions());
    }

    public static TeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(Teacher teacher,
            ExecutionYear executionYear) {
        return teacher.getTeacherPersonalExpectationsSet().stream().filter(e -> e.getExecutionYear().equals(executionYear))
                .findAny().orElse(null);
    }

}
