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
package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.students;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/student",
        functionality = SearchForStudentsForInternationalRelatOffice.class)
@Forwards(value = { @Forward(name = "viewStudentDetails", path = "/internationalRelatOffice/viewStudentDetails.jsp"),
        @Forward(name = "viewRegistrationDetails", path = "/internationalRelatOffice/viewRegistrationDetails.jsp"),
        @Forward(name = "viewPersonalData", path = "/internationalRelatOffice/viewPersonalData.jsp") })
public class StudentDAForInternationalRelatOffice extends StudentDA {
}