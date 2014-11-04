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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentDataBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

public class PrecedentDegreeInformationForIndividualCandidacyFactory {

    /* CREATE */

    public static PrecedentDegreeInformation create(final IndividualCandidacy individualCandidacy,
            final IndividualCandidacyProcessBean processBean) {

        if (processBean.isStandalone()) {
            return null;
        }

        PrecedentDegreeInformation pid = null;
        if (processBean.isDegreeChange() || processBean.isDegreeTransfer()) {
            pid = createForDegreeTransferOrDegreeChange(processBean);
        } else if (processBean.isSecondCycle() || processBean.isDegreeCandidacyForGraduatedPerson()) {
            pid = createForSecondCycleOrDegreeCandidacyForGraduatedPerson(processBean);
        } else if (processBean.isOver23()) {
            pid = createForOver23(processBean);
        } else if (processBean.isErasmus()) {
            pid = createForErasmus(processBean);
        }

        pid.setIndividualCandidacy(individualCandidacy);

        return pid;
    }

    private static PrecedentDegreeInformation createForErasmus(final IndividualCandidacyProcessBean processBean) {
        MobilityIndividualApplicationProcessBean erasmusBean = (MobilityIndividualApplicationProcessBean) processBean;
        MobilityStudentDataBean erasmusStudentDataBean = erasmusBean.getMobilityStudentDataBean();

        PrecedentDegreeInformation pdi = new PrecedentDegreeInformation();
        pdi.setCandidacyInternal(false);

        pdi.setPrecedentCountry(erasmusStudentDataBean.getSelectedCountry());
        pdi.setPrecedentInstitution(erasmusStudentDataBean.getSelectedUniversity());
        pdi.setPrecedentSchoolLevel(erasmusStudentDataBean.getCurrentSchoolLevel());
        pdi.setOtherPrecedentSchoolLevel(erasmusStudentDataBean.getCurrentOtherSchoolLevel());
        pdi.setCountryHighSchool(erasmusStudentDataBean.getCountryWhereFinishedHighSchoolLevel());

        if (erasmusStudentDataBean.getHasDiplomaOrDegree()) {
            pdi.setDegreeDesignation(erasmusStudentDataBean.getDiplomaName());
            pdi.setConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
        }

        return pdi;
    }

    private static PrecedentDegreeInformation createForOver23(IndividualCandidacyProcessBean processBean) {
        Over23IndividualCandidacyProcessBean over23Bean = (Over23IndividualCandidacyProcessBean) processBean;

        if (!over23Bean.getFormationConcludedBeanList().isEmpty()) {
            PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
            pid.setCandidacyInternal(false);
            FormationBean formationBean = over23Bean.getFormationConcludedBeanList().iterator().next();

            pid.setDegreeDesignation(formationBean.getDesignation());
            pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
            pid.setConclusionYear(formationBean.getConclusionExecutionYear().getEndCivilYear());

            return pid;
        } else if (!over23Bean.getFormationNonConcludedBeanList().isEmpty()) {
            PrecedentDegreeInformation pdi = new PrecedentDegreeInformation();
            pdi.setCandidacyInternal(false);
            FormationBean formationBean = over23Bean.getFormationNonConcludedBeanList().iterator().next();

            pdi.setDegreeDesignation(formationBean.getDesignation());
            pdi.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));

            return pdi;
        }

        return null;
    }

    private static PrecedentDegreeInformation createForDegreeTransferOrDegreeChange(IndividualCandidacyProcessBean processBean) {
        IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPDIBean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
        PrecedentDegreeInformationBean bean = candidacyProcessWithPDIBean.getPrecedentDegreeInformation();

        PrecedentDegreeInformation pdi = new PrecedentDegreeInformation();
        pdi.setPrecedentDegreeDesignation(bean.getDegreeDesignation());
        pdi.setNumberOfEnroledCurricularCourses(bean.getNumberOfEnroledCurricularCourses());
        pdi.setNumberOfApprovedCurricularCourses(bean.getNumberOfApprovedCurricularCourses());
        pdi.setGradeSum(bean.getGradeSum());
        pdi.setApprovedEcts(bean.getApprovedEcts());
        pdi.setEnroledEcts(bean.getEnroledEcts());

        if (candidacyProcessWithPDIBean.isExternalPrecedentDegreeType()) {
            pdi.setPrecedentInstitution(getOrCreateInstitution(bean));
            pdi.setNumberOfEnrolmentsInPreviousDegrees(candidacyProcessWithPDIBean
                    .getNumberOfPreviousYearEnrolmentsInPrecedentDegree());
            pdi.setCandidacyInternal(false);
        } else {
            pdi.setCandidacyInternal(true);
            final StudentCurricularPlan studentCurricularPlan = candidacyProcessWithPDIBean.getPrecedentStudentCurricularPlan();

            if (studentCurricularPlan == null) {
                throw new DomainException("error.IndividualCandidacy.invalid.precedentDegreeInformation");
            }

            pdi.setStudentCurricularPlan(studentCurricularPlan);
        }

        return pdi;
    }

    private static PrecedentDegreeInformation createForSecondCycleOrDegreeCandidacyForGraduatedPerson(
            IndividualCandidacyProcessBean processBean) {
        IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;

        PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

        PrecedentDegreeInformation pdi = new PrecedentDegreeInformation();
        pdi.setCandidacyInternal(false);
        pdi.setDegreeDesignation(bean.getDegreeDesignation());
        pdi.setInstitution(getOrCreateInstitution(bean));
        pdi.setCountry(bean.getCountry());
        pdi.setConclusionDate(bean.getConclusionDate());

        if (bean.getConclusionDate() != null) {
            pdi.setConclusionYear(bean.getConclusionDate().getYear());
        }

        pdi.setConclusionGrade(bean.getConclusionGrade());

        return pdi;
    }

    private static Unit getOrCreateInstitution(final PrecedentDegreeInformationBean bean) {
        if (bean.getInstitution() != null) {
            return bean.getInstitution();
        }
        return getOrCreateInstitution(bean.getInstitutionName());
    }

    private static Unit getOrCreateInstitution(final String institutionName) {
        if (institutionName == null || institutionName.isEmpty()) {
            throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
        }

        final Unit unit = Unit.findFirstExternalUnitByName(institutionName);
        return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(institutionName);
    }

    /* EDIT */

    public static final void edit(final IndividualCandidacyProcessBean processBean) {
        if (processBean.isDegreeChange() || processBean.isDegreeTransfer()) {
            editForDegreeTransferOrDegreeChange(processBean);
        } else if (processBean.isSecondCycle() || processBean.isDegreeCandidacyForGraduatedPerson()) {
            editForSecondCycleOrDegreeCandidacyForGraduatedPerson(processBean);
        } else if (processBean.isOver23()) {
            editForOver23(processBean);
        } else if (processBean.isErasmus()) {
            editForErasmus(processBean);
        }
    }

    private static void editForErasmus(IndividualCandidacyProcessBean processBean) {
        MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) processBean;
        MobilityStudentDataBean erasmusStudentDataBean = bean.getMobilityStudentDataBean();

        MobilityIndividualApplicationProcess erasmusIndividualCandidacyProcess =
                (MobilityIndividualApplicationProcess) processBean.getIndividualCandidacyProcess();
        MobilityIndividualApplication erasmusCandidacy = erasmusIndividualCandidacyProcess.getCandidacy();

        PrecedentDegreeInformation pid = erasmusCandidacy.getRefactoredPrecedentDegreeInformation();

        pid.setPrecedentCountry(erasmusStudentDataBean.getSelectedCountry());
        pid.setPrecedentInstitution(erasmusStudentDataBean.getSelectedUniversity());
        pid.setPrecedentSchoolLevel(erasmusStudentDataBean.getCurrentSchoolLevel());
        pid.setOtherPrecedentSchoolLevel(erasmusStudentDataBean.getCurrentOtherSchoolLevel());
        pid.setCountryHighSchool(erasmusStudentDataBean.getCountryWhereFinishedHighSchoolLevel());

        if (erasmusStudentDataBean.getHasDiplomaOrDegree()) {
            pid.setDegreeDesignation(erasmusStudentDataBean.getDiplomaName());
            pid.setConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
        }
    }

    private static void editForOver23(IndividualCandidacyProcessBean processBean) {
        Over23IndividualCandidacyProcessBean over23Bean = (Over23IndividualCandidacyProcessBean) processBean;

        IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
        IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
        PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

        if (!over23Bean.getFormationConcludedBeanList().isEmpty()) {
            FormationBean formationBean = over23Bean.getFormationConcludedBeanList().iterator().next();

            pid.setDegreeDesignation(formationBean.getDesignation());
            pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
            pid.setConclusionYear(formationBean.getConclusionExecutionYear().getEndCivilYear());
        } else if (over23Bean.getFormationNonConcludedBeanList().isEmpty()) {
            FormationBean formationBean = over23Bean.getFormationNonConcludedBeanList().iterator().next();

            pid.setDegreeDesignation(formationBean.getDesignation());
            pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
        }

    }

    private static void editForSecondCycleOrDegreeCandidacyForGraduatedPerson(IndividualCandidacyProcessBean processBean) {
        IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
        IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
        IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
        PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

        PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

        pid.setDegreeDesignation(bean.getDegreeDesignation());
        pid.setInstitution(getOrCreateInstitution(bean));
        pid.setCountry(bean.getCountry());
        pid.setConclusionDate(bean.getConclusionDate());

        if (bean.getConclusionDate() != null) {
            pid.setConclusionYear(bean.getConclusionDate().getYear());
        }

        pid.setConclusionGrade(bean.getConclusionGrade());

    }

    private static void editForDegreeTransferOrDegreeChange(IndividualCandidacyProcessBean processBean) {
        IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
        PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

        IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
        IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
        PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

        pid.setPrecedentDegreeDesignation(bean.getDegreeDesignation());
        pid.setPrecedentInstitution(getOrCreateInstitution(bean));
        pid.setNumberOfEnroledCurricularCourses(bean.getNumberOfEnroledCurricularCourses());
        pid.setNumberOfApprovedCurricularCourses(bean.getNumberOfApprovedCurricularCourses());
        pid.setGradeSum(bean.getGradeSum());
        pid.setApprovedEcts(bean.getApprovedEcts());
        pid.setEnroledEcts(bean.getEnroledEcts());
    }

}
