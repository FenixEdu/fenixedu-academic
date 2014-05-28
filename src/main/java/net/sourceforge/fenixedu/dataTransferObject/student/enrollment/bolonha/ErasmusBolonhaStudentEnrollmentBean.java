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
package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;

public class ErasmusBolonhaStudentEnrollmentBean extends BolonhaStudentEnrollmentBean {

    public static class ErasmusExtraCurricularEnrolmentBean implements Serializable {
        private final CurricularCourse curricularCourse;
        private final Boolean enrol;

        public ErasmusExtraCurricularEnrolmentBean(CurricularCourse curricularCourse, Boolean enrol) {
            this.curricularCourse = curricularCourse;
            this.enrol = enrol;
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourse;
        }

        public Boolean getEnrol() {
            return enrol;
        }
    }

    private MobilityIndividualApplication candidacy;
    private List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments = null;

    public ErasmusBolonhaStudentEnrollmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
            int[] curricularYears, CurricularRuleLevel curricularRuleLevel, MobilityIndividualApplication candidacy) {
        super(studentCurricularPlan, executionSemester, curricularYears, curricularRuleLevel);
        setCandidacy(candidacy);
    }

    public void setCandidacy(MobilityIndividualApplication candidacy) {
        this.candidacy = candidacy;
    }

    public MobilityIndividualApplication getCandidacy() {
        return candidacy;
    }

    public void setExtraCurricularEnrolments(List<ErasmusExtraCurricularEnrolmentBean> extraCurricularEnrolments) {
        this.extraCurricularEnrolments = extraCurricularEnrolments;
    }

    public List<ErasmusExtraCurricularEnrolmentBean> getExtraCurricularEnrolments() {
        return extraCurricularEnrolments;
    }
}
