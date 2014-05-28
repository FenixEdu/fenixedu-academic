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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class InfoCandidateRegistration extends InfoObject {

    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    private InfoMasterDegreeCandidate infoMasterDegreeCandidate;

    private List<InfoEnrolment> enrolments;

    /**
     * @return
     */
    public List<InfoEnrolment> getEnrolments() {
        return enrolments;
    }

    /**
     * @param enrolments
     */
    public void setEnrolments(List<InfoEnrolment> enrolments) {
        this.enrolments = enrolments;
    }

    /**
     * @return
     */
    public InfoMasterDegreeCandidate getInfoMasterDegreeCandidate() {
        return infoMasterDegreeCandidate;
    }

    /**
     * @param infoMasterDegreeCandidate
     */
    public void setInfoMasterDegreeCandidate(InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
        this.infoMasterDegreeCandidate = infoMasterDegreeCandidate;
    }

    /**
     * @return
     */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    /**
     * @param infoStudentCurricularPlan
     */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    public InfoCandidateRegistration() {
    }

}