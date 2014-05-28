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
package net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadProfessorshipsAndResponsibilitiesByExecutionDegreeAndExecutionPeriod {

    @Atomic
    public static List run(String executionDegreeId, Integer semester, Integer teacherType) throws FenixServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        List professorships;
        if (semester.intValue() == 0) {
            professorships =
                    Professorship.readByDegreeCurricularPlanAndExecutionYear(executionDegree.getDegreeCurricularPlan(),
                            executionDegree.getExecutionYear());
        } else {
            ExecutionSemester executionSemester = executionDegree.getExecutionYear().getExecutionSemesterFor(semester);
            professorships =
                    Professorship.readByDegreeCurricularPlanAndExecutionPeriod(executionDegree.getDegreeCurricularPlan(),
                            executionSemester);
        }

        List responsibleFors = getResponsibleForsByDegree(executionDegree);

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, teacherType);

        // Cleaning out possible null elements inside the list
        Iterator itera = detailedProfessorships.iterator();
        while (itera.hasNext()) {
            Object dp = itera.next();
            if (dp == null) {
                itera.remove();
            }
        }

        Collections.sort(detailedProfessorships, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                int result =
                        detailedProfessorship1
                                .getInfoProfessorship()
                                .getInfoExecutionCourse()
                                .getExternalId()
                                .compareTo(detailedProfessorship2.getInfoProfessorship().getInfoExecutionCourse().getExternalId());
                if (result == 0
                        && (detailedProfessorship1.getResponsibleFor().booleanValue() || detailedProfessorship2
                                .getResponsibleFor().booleanValue())) {
                    if (detailedProfessorship1.getResponsibleFor().booleanValue()) {
                        return -1;
                    }
                    if (detailedProfessorship2.getResponsibleFor().booleanValue()) {
                        return 1;
                    }
                }

                return result;
            }

        });

        List result = new ArrayList();
        Iterator iter = detailedProfessorships.iterator();
        List temp = new ArrayList();
        while (iter.hasNext()) {
            DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter.next();
            if (temp.isEmpty()
                    || ((DetailedProfessorship) temp.get(temp.size() - 1)).getInfoProfessorship().getInfoExecutionCourse()
                            .equals(detailedProfessorship.getInfoProfessorship().getInfoExecutionCourse())) {
                temp.add(detailedProfessorship);
            } else {
                result.add(temp);
                temp = new ArrayList();
                temp.add(detailedProfessorship);
            }
        }
        if (!temp.isEmpty()) {
            result.add(temp);
        }
        return result;
    }

    private static List getResponsibleForsByDegree(ExecutionDegree executionDegree) {
        List responsibleFors = new ArrayList();

        Collection<ExecutionCourse> executionCourses = new ArrayList();
        Collection<ExecutionSemester> executionSemesters = executionDegree.getExecutionYear().getExecutionPeriods();

        for (ExecutionSemester executionSemester : executionSemesters) {
            executionCourses = executionSemester.getAssociatedExecutionCourses();
            for (ExecutionCourse executionCourse : executionCourses) {
                responsibleFors.add(executionCourse.responsibleFors());
            }
        }
        return responsibleFors;
    }

    protected static List getDetailedProfessorships(List professorships, final List responsibleFors, final Integer teacherType) {
        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships, new Transformer() {

            @Override
            public Object transform(Object input) {
                Professorship professorship = (Professorship) input;

                InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

                List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship.getExecutionCourse());

                DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                Boolean isResponsible = Boolean.valueOf(professorship.getResponsibleFor());

                if ((teacherType.intValue() == 1) && (!isResponsible.booleanValue())) {
                    return null;
                }

                detailedProfessorship.setResponsibleFor(isResponsible);

                detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                detailedProfessorship.setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                return detailedProfessorship;
            }

            private List getInfoCurricularCourses(ExecutionCourse executionCourse) {

                List infoCurricularCourses =
                        (List) CollectionUtils.collect(executionCourse.getAssociatedCurricularCourses(), new Transformer() {

                            @Override
                            public Object transform(Object input) {
                                CurricularCourse curricularCourse = (CurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse =
                                        InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                                return infoCurricularCourse;
                            }
                        });
                return infoCurricularCourses;
            }
        });

        return detailedProfessorshipList;
    }

}