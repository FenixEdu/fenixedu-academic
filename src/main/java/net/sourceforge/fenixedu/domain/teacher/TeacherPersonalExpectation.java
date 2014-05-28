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
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAutoEvaluationDefinitionPeriod;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsEvaluationPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
            if (teacher.getTeacherPersonalExpectationByExecutionYear(executionYear) != null) {
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
        Department department = getTeacher().getCurrentWorkingDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    department.getTeacherExpectationDefinitionPeriodForExecutionYear(getExecutionYear());
            return (period == null) ? false : period.isPeriodOpen();
        }
        return false;
    }

    public boolean isAllowedToEditAutoEvaluation() {
        Department department = getTeacher().getCurrentWorkingDepartment();
        if (department != null) {
            TeacherAutoEvaluationDefinitionPeriod period =
                    department.getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(getExecutionYear());
            return (period == null) ? false : period.isPeriodOpen();
        }
        return false;
    }

    public boolean isAllowedToEditEvaluation() {
        Department department = getTeacher().getCurrentWorkingDepartment();
        if (department != null) {
            TeacherPersonalExpectationsEvaluationPeriod period =
                    department.getTeacherPersonalExpectationsEvaluationPeriodByExecutionYear(getExecutionYear());
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

    @Deprecated
    public boolean hasGraduationsDescription() {
        return getGraduationsDescription() != null;
    }

    @Deprecated
    public boolean hasOrientationsMainFocus() {
        return getOrientationsMainFocus() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTutorComment() {
        return getTutorComment() != null;
    }

    @Deprecated
    public boolean hasCientificPosGraduationsDescription() {
        return getCientificPosGraduationsDescription() != null;
    }

    @Deprecated
    public boolean hasPatentPublications() {
        return getPatentPublications() != null;
    }

    @Deprecated
    public boolean hasEducationMainFocus() {
        return getEducationMainFocus() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeOrientations() {
        return getMasterDegreeOrientations() != null;
    }

    @Deprecated
    public boolean hasUniversityOrgans() {
        return getUniversityOrgans() != null;
    }

    @Deprecated
    public boolean hasSocietyService() {
        return getSocietyService() != null;
    }

    @Deprecated
    public boolean hasResearchAndDevProjects() {
        return getResearchAndDevProjects() != null;
    }

    @Deprecated
    public boolean hasConsulting() {
        return getConsulting() != null;
    }

    @Deprecated
    public boolean hasCientificPosGraduations() {
        return getCientificPosGraduations() != null;
    }

    @Deprecated
    public boolean hasResearchAndDevMainFocus() {
        return getResearchAndDevMainFocus() != null;
    }

    @Deprecated
    public boolean hasConferencePublications() {
        return getConferencePublications() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeWorkOrientations() {
        return getFinalDegreeWorkOrientations() != null;
    }

    @Deprecated
    public boolean hasSeminariesDescription() {
        return getSeminariesDescription() != null;
    }

    @Deprecated
    public boolean hasProfessionalPosGraduations() {
        return getProfessionalPosGraduations() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    @Deprecated
    public boolean hasJornalArticlePublications() {
        return getJornalArticlePublications() != null;
    }

    @Deprecated
    public boolean hasDepartmentOrgans() {
        return getDepartmentOrgans() != null;
    }

    @Deprecated
    public boolean hasBookPublications() {
        return getBookPublications() != null;
    }

    @Deprecated
    public boolean hasPhdOrientations() {
        return getPhdOrientations() != null;
    }

    @Deprecated
    public boolean hasProfessionalPosGraduationsDescription() {
        return getProfessionalPosGraduationsDescription() != null;
    }

    @Deprecated
    public boolean hasAutoEvaluation() {
        return getAutoEvaluation() != null;
    }

    @Deprecated
    public boolean hasTechnicalReportPublications() {
        return getTechnicalReportPublications() != null;
    }

    @Deprecated
    public boolean hasCompanyPositions() {
        return getCompanyPositions() != null;
    }

    @Deprecated
    public boolean hasSeminaries() {
        return getSeminaries() != null;
    }

    @Deprecated
    public boolean hasOtherPublicationsDescription() {
        return getOtherPublicationsDescription() != null;
    }

    @Deprecated
    public boolean hasCientificComunityService() {
        return getCientificComunityService() != null;
    }

    @Deprecated
    public boolean hasGraduations() {
        return getGraduations() != null;
    }

    @Deprecated
    public boolean hasCompanySocialOrgans() {
        return getCompanySocialOrgans() != null;
    }

    @Deprecated
    public boolean hasOtherPublications() {
        return getOtherPublications() != null;
    }

    @Deprecated
    public boolean hasInstitutionOrgans() {
        return getInstitutionOrgans() != null;
    }

    @Deprecated
    public boolean hasUniversityServiceMainFocus() {
        return getUniversityServiceMainFocus() != null;
    }

    @Deprecated
    public boolean hasProfessionalActivityMainFocus() {
        return getProfessionalActivityMainFocus() != null;
    }

}
