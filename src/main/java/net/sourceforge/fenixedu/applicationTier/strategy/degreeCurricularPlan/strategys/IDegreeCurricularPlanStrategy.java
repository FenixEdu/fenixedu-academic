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

import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IDegreeCurricularPlanStrategy {

    /**
     * Gets the Degree Curricular Plan
     * 
     * @return the DegreeCurricular Plan
     */
    public DegreeCurricularPlan getDegreeCurricularPlan();

    /**
     * Checks if the mark is Valid for this Degree Curricular Plan
     * 
     * @param The
     *            String with the mark to test
     */
    public boolean checkMark(String mark);

    public boolean checkMark(String mark, EvaluationType et);

    /**
     * Calculate's the Registration's regular average
     * 
     * @param The
     *            student's Curricular Plan
     * @param The
     *            list of the students enrolment
     * @return The Registration's Average
     */
    public Double calculateStudentRegularAverage(StudentCurricularPlan studentCurricularPlan);

    /**
     * Calculate's the Registration's weighted average
     * 
     * @param The
     *            student's Curricular Plan
     * @param The
     *            list of the students enrolment
     * @return The Registration's Average
     */
    public Double calculateStudentWeightedAverage(StudentCurricularPlan studentCurricularPlan);

    /**
     * 
     * @param studentCurricularPlan
     * @param infoFinalResult
     * @throws ExcepcaoPersistencia
     */
    public void calculateStudentAverage(StudentCurricularPlan studentCurricularPlan, InfoFinalResult infoFinalResult);

}