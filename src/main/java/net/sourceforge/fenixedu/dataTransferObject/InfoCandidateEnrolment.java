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

import net.sourceforge.fenixedu.domain.CandidateEnrolment;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateEnrolment extends InfoObject {
    private InfoMasterDegreeCandidate infoMasterDegreeCandidate;

    private InfoCurricularCourseScope infoCurricularCourseScope;

    private InfoCurricularCourse infoCurricularCourse;

    public InfoCandidateEnrolment() {
    }

    public InfoCandidateEnrolment(InfoMasterDegreeCandidate infoMasterDegreeCandidate, InfoCurricularCourse infoCurricularCourse) {
        setInfoMasterDegreeCandidate(infoMasterDegreeCandidate);
        setInfoCurricularCourse(infoCurricularCourse);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoCandidateEnrolment) {
            InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) obj;
            result =
                    getInfoMasterDegreeCandidate().equals(infoCandidateEnrolment.getInfoMasterDegreeCandidate())
                            && getInfoCurricularCourse().equals(infoCandidateEnrolment.getInfoCurricularCourse());
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[CANDIDATE_ENROLMENT";
        result += ", codInt=" + getExternalId();
        result += ", infoMasterDegreeCandidate=" + infoMasterDegreeCandidate;
        result += ", infoCurricularCourse=" + infoCurricularCourse;
        result += "]";
        return result;
    }

    /**
     * @return @deprecated
     */
    public InfoCurricularCourseScope getInfoCurricularCourseScope() {
        return infoCurricularCourseScope;
    }

    /**
     * @return
     */
    public InfoMasterDegreeCandidate getInfoMasterDegreeCandidate() {
        return infoMasterDegreeCandidate;
    }

    /**
     * @param course
     * @deprecated
     */
    @Deprecated
    public void setInfoCurricularCourseScope(InfoCurricularCourseScope courseScope) {
        infoCurricularCourseScope = courseScope;
    }

    /**
     * @param candidate
     */
    public void setInfoMasterDegreeCandidate(InfoMasterDegreeCandidate candidate) {
        infoMasterDegreeCandidate = candidate;
    }

    /**
     * @return
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    public void copyFromDomain(CandidateEnrolment candidateEnrolment) {
        super.copyFromDomain(candidateEnrolment);
    }

    public static InfoCandidateEnrolment newInfoFromDomain(CandidateEnrolment candidateEnrolment) {
        InfoCandidateEnrolment infoCandidateEnrolment = null;
        if (candidateEnrolment != null) {
            infoCandidateEnrolment = new InfoCandidateEnrolment();
            infoCandidateEnrolment.copyFromDomain(candidateEnrolment);
        }
        return infoCandidateEnrolment;
    }

}