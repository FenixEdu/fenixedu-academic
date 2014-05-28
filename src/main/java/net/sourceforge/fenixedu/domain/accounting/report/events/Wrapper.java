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
package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public interface Wrapper {
    public static final String PHD_PROGRAM_STUDIES = "Programa Doutoral";
    public static final String REGISTRATION_STUDIES = "Curso";

    public String getStudentNumber();

    public String getStudentName();

    public String getStudentEmail();

    public String getRegistrationStartDate();

    public String getExecutionYear();

    public String getDegreeName();

    public String getDegreeType();

    public String getPhdProgramName();

    public String getEnrolledECTS();

    public String getRegime();

    public String getEnrolmentModel();

    public String getResidenceYear();

    public String getResidenceMonth();

    public String getStudiesType();

    public String getTotalDiscount();

    public String getRelatedEventExternalId();

    public String getDebtorFiscalId();

    public String getDebtorName();

    public boolean isAfterOrEqualExecutionYear(final ExecutionYear executionYear);

    public ExecutionYear getForExecutionYear();

    public AdministrativeOffice getRelatedAcademicOffice();
}
