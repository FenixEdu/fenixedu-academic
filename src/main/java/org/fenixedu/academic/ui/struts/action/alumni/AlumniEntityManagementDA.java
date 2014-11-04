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
package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.RegisterAlumniData;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class AlumniEntityManagementDA extends FenixDispatchAction {

    public AlumniEntityManagementDA() {
        super();
    }

    protected Alumni getAlumniFromLoggedPerson(HttpServletRequest request) {
        Student alumniStudent = getLoggedPerson(request).getStudent();
        if (alumniStudent.getAlumni() != null) {
            return alumniStudent.getAlumni();
        } else {
            return RegisterAlumniData.run(alumniStudent);
        }
    }

}