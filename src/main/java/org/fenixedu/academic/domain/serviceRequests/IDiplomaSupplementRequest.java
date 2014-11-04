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
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Locale;

import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;

public interface IDiplomaSupplementRequest extends IDocumentRequest {
    public CycleType getRequestedCycle();

    public String getGraduateTitle(final Locale locale);

    public Integer getRegistrationNumber();

    public String getPrevailingScientificArea(final Locale locale);

    public long getEctsCredits();

    public DegreeOfficialPublication getDegreeOfficialPublication();

    public Integer getFinalAverage();

    public String getFinalAverageQualified(final Locale locale);

    public ExecutionYear getConclusionYear();

    public EctsGraduationGradeConversionTable getGraduationConversionTable();

    public Integer getNumberOfCurricularYears();

    public Integer getNumberOfCurricularSemesters();

    public Boolean isExemptedFromStudy();

    public Registration getRegistration();

    public boolean hasRegistration();
}
