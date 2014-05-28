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
package net.sourceforge.fenixedu.domain.student.scholarship.report;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.util.Money;

public interface IUTLScholarshipReportResult {

    static final String INSTITUTION_CODE = "807";
    static final String INTEGRATED_MASTER_DESIGNATION = "Mestrado Integrado";
    static final String BOLONHA_DEGREE_DESIGNATION = "Licenciatura 1º Ciclo";

    static final List<Integer> STUDENTS_WITH_CET = Arrays.asList(new Integer[] { 70855, 70696, 70757, 70786, 55647, 59218, 70749,
            70856, 70678, 70681, 70712, 70737, 70837, 70793, 10425, 38565, 70783, 70664, 70859, 70766, 70844, 48936, 50315,
            70788, 70794, 70795, 70804, 70809, 70716, 70719, 70763, 70776, 70841, 70923 });

    static final List<QualificationType> DEGREE_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.BACHELOR_AND_DEGREE, QualificationType.BACHELOR_DEGREE,
            QualificationType.BACHELOR_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DEGREE,
            QualificationType.DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.INTEGRATED_MASTER_DEGREE, });

    static final List<QualificationType> MASTER_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.INTEGRATED_MASTER_DEGREE, QualificationType.MASTER,
            QualificationType.MASTER_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.MASTER_DEGREE_WITH_RECOGNITION });

    public static final List<QualificationType> PHD_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.DOCTORATE_DEGREE, QualificationType.DOCTORATE_DEGREE_BOLOGNA,
            QualificationType.DOCTORATE_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DOCTORATE_DEGREE_WITH_RECOGNITION,
            QualificationType.DOCTORATE_DEGREE_WITH_REGISTER });

    public String getInstitutionCode();

    public String getInstitutionName();

    public String getApplicationNumber();

    public Integer getStudentNumber();

    public String getStudentName();

    public String getIdDocumentType();

    public String getIdDocumentNumber();

    public String getDegreeCode();

    public String getDegreeName();

    public String getDegreeTypeName();

    public Integer getNumberOfDegreeChanges();

    public String getHasMadeDegreeChangeInThisExecutionYear();

    public String getCurrentExecutionYearBeginDate();

    public Integer getNumberOfStudyExecutionYearsInCurrentRegistration();

    public String getRegimen();

    public String getCode();

    public String getFirstExecutionYearInIST();

    public Integer getNumberOfEnrolmentsYearsSinceRegistrationStart();

    public Integer getNumberOfCurricularYearsOnCurrentDegreeCurricularPlan();

    public Integer getLastYearCurricularYear();

    public BigDecimal getLastYearEnrolledECTS();

    public BigDecimal getLastYearApprovedECTS();

    public String getWasApprovedOnMostECTS();

    public Integer getCurrentYearCurricularYear();

    public BigDecimal getCurrentYearEnrolledECTS();

    public String getDegreeConcluded();

    public String getFinalResult();

    public Money getGratuityAmount();

    public Integer getNumberOfMonthsInExecutionYear();

    public String getFirstMonthToPay();

    public String getIsCETQualificationOwner();

    public String getIsDegreeQualificationOwner();

    public String getIsMasterDegreeQualificationOwner();

    public String getIsPhdQualificationOwner();

    public String getIsOwnerOfQualification();

    public String getObservations();

}
