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

import java.util.Date;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IMasterDegreeCurricularPlanStrategy extends IDegreeCurricularPlanStrategy {

    /**
     * Checks if the Master Degree Registration has finished his scholar part. <br/>
     * All his credits are added and compared to the ones required by his Degree
     * Curricular Plan.
     * 
     * @param The
     *            Registration's Curricular Plan
     * @return A boolean indicating if he has finished it or not.
     */
    public boolean checkEndOfScholarship(StudentCurricularPlan studentCurricularPlan);

    /**
     * 
     * @param studentCurricularPlan
     * @return The Date of the student's end of his scholar part.
     * @throws ExcepcaoPersistencia
     */
    public Date dateOfEndOfScholarship(StudentCurricularPlan studentCurricularPlan);

}