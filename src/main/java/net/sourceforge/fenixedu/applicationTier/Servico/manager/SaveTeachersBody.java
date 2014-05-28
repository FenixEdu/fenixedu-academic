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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SaveTeachersBody {

    protected Boolean run(final List<String> responsibleTeachersIds, final List<String> professorShipTeachersIds,
            final String executionCourseId) throws FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);

        final List<String> auxProfessorshipTeacherIDs = new ArrayList<String>(professorShipTeachersIds);

        final List<Professorship> professorships = new ArrayList<Professorship>(executionCourse.getProfessorshipsSet());
        for (Professorship professorship : professorships) {
            final Teacher teacher = professorship.getTeacher();
            final String teacherID = teacher.getExternalId();
            if (auxProfessorshipTeacherIDs.contains(teacherID)) {
                if (responsibleTeachersIds.contains(teacherID) && !professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.TRUE);
                } else if (!responsibleTeachersIds.contains(teacherID) && professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.FALSE);
                }
                auxProfessorshipTeacherIDs.remove(teacherID);
            } else {
                professorship.delete();
            }
        }

        for (final String teacherID : auxProfessorshipTeacherIDs) {
            final Teacher teacher = FenixFramework.getDomainObject(teacherID);
            final Boolean isResponsible = Boolean.valueOf(responsibleTeachersIds.contains(teacherID));
            Professorship.create(isResponsible, executionCourse, teacher, null);
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final SaveTeachersBody serviceInstance = new SaveTeachersBody();

    @Atomic
    public static Boolean runSaveTeachersBody(List<String> responsibleTeachersIds, List<String> professorShipTeachersIds,
            String executionCourseId) throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(responsibleTeachersIds, professorShipTeachersIds, executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(responsibleTeachersIds, professorShipTeachersIds, executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}