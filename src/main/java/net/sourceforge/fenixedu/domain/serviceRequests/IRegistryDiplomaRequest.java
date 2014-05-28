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
package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

import org.joda.time.LocalDate;

public interface IRegistryDiplomaRequest extends IDocumentRequest {
    public LocalDate getConclusionDate();

    public ExecutionYear getConclusionYear();

    public String getGraduateTitle(Locale locale);

    public CycleType getRequestedCycle();

    public String getFinalAverage(final Locale locale);

    public String getQualifiedAverageGrade(final Locale locale);

    public IDiplomaSupplementRequest getDiplomaSupplement();
}
