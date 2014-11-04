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
package org.fenixedu.academic.service.services.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAdmitedCandidates {

    /**
     * 
     * @param candidateList
     * @return The candidates Admited for Master Degree (Specialization not
     *         Included) This is only for Numerus Clauses count
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */
    @Atomic
    public static List run(String[] candidateList, String[] ids) throws FenixServiceException {
        List<String> result = new ArrayList<String>();

        // Read the admited candidates
        int size = candidateList.length;
        int i = 0;
        for (i = 0; i < size; i++) {
            if (candidateList[i].equals(SituationName.ADMITIDO_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_SPECIALIZATION_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING)) {

                MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(ids[i]);
                if (!masterDegreeCandidate.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
                    result.add(candidateList[i]);
                }
            }
        }

        return result;
    }

}