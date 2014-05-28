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
package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements
        IMasterDegreeCurricularPlanStrategy {

    public MasterDegreeCurricularPlanStrategy(DegreeCurricularPlan degreeCurricularPlan) {
        super(degreeCurricularPlan);
    }

    /**
     * Checks if the Master Degree Registration has finished his scholar part. <br/>
     * All his credits are added and compared to the ones required by his Degree
     * Curricular Plan.
     * 
     * @param The
     *            Registration's Curricular Plan
     * @return A boolean indicating if he has fineshed it or not.
     */
    @Override
    public boolean checkEndOfScholarship(StudentCurricularPlan studentCurricularPlan) {
        double studentCredits = 0;

        DegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();

        Collection enrolments = studentCurricularPlan.getEnrolmentsSet();

        Iterator iterator = enrolments.iterator();

        if (studentCurricularPlan.getGivenCredits() != null) {
            studentCredits += studentCurricularPlan.getGivenCredits().doubleValue();
        }

        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();

            if (enrolment.isEnrolmentStateApproved() && !enrolment.isExtraCurricular()
                    && enrolment.getCurricularCourse().getCredits() != null) {
                studentCredits += enrolment.getCurricularCourse().getCredits().doubleValue();
            }
        }

        if (degreeCurricularPlan.getNeededCredits() != null) {
            return (studentCredits >= degreeCurricularPlan.getNeededCredits().doubleValue());
        }
        return true;
    }

    @Override
    public Date dateOfEndOfScholarship(StudentCurricularPlan studentCurricularPlan) {

        Date date = null;
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;

        // float studentCredits = 0;
        //		
        // DegreeCurricularPlan degreeCurricularPlan =
        // super.getDegreeCurricularPlan();

        Collection enrolments = studentCurricularPlan.getEnrolmentsSet();

        Iterator iterator = enrolments.iterator();

        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();
            if (enrolment.isEnrolmentStateApproved()) {

                infoEnrolmentEvaluation = new GetEnrolmentGrade().run(enrolment);

                if (infoEnrolmentEvaluation.getExamDate() == null) {
                    continue;
                }

                if (date == null) {
                    date = new Date(infoEnrolmentEvaluation.getExamDate().getTime());
                    continue;
                }

                if (infoEnrolmentEvaluation.getExamDate().after(date)) {
                    date.setTime(infoEnrolmentEvaluation.getExamDate().getTime());
                }

            }
        }

        if (date == null && studentCurricularPlan.getMasterDegreeThesis() != null) {
            MasterDegreeProofVersion proofVersion =
                    studentCurricularPlan.getMasterDegreeThesis().getActiveMasterDegreeProofVersion();
            if (proofVersion != null) {
                date = proofVersion.getProofDate();
            }
        }

        return date;
    }

}