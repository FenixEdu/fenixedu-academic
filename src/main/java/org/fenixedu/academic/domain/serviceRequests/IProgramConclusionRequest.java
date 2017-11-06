/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Locale;
import java.util.Optional;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.commons.i18n.LocalizedString;

public interface IProgramConclusionRequest extends IDocumentRequest {

    CycleType getRequestedCycle();

    ProgramConclusion getProgramConclusion();

    String getGraduateTitle(final Locale locale);

    Degree getDegree();

    ExecutionYear getConclusionYear();

    default Optional<LocalizedString> getAssociatedInstitutions() {
        ExecutionYear conclusionYear = getConclusionYear();
        if (conclusionYear == null) {
            return Optional.empty();
        }
        DegreeInfo degreeInfoFor = getDegree().getDegreeInfoFor(conclusionYear);
        if (degreeInfoFor != null || (degreeInfoFor = getDegree().getMostRecentDegreeInfo()) != null) {
            return Optional.of(degreeInfoFor.getAssociatedInstitutions());
        }
        return Optional.empty();
    }

    default Optional<String> getAssociatedInstitutionsContent() {
        return getAssociatedInstitutions().map(l -> l.getContent(getLanguage()));
    }
}
