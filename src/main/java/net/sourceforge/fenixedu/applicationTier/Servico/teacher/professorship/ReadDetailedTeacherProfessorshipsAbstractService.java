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
/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsAbstractService {

    private final class Professorships2DetailProfessorship implements Transformer {
        private Professorships2DetailProfessorship() {
            super();
        }

        @Override
        public Object transform(Object input) {
            Professorship professorship = (Professorship) input;
            InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

            final DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            List executionCourseCurricularCoursesList = getInfoCurricularCourses(detailedProfessorship, executionCourse);

            detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
            detailedProfessorship.setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

            return detailedProfessorship;
        }

        private List getInfoCurricularCourses(final DetailedProfessorship detailedProfessorship, ExecutionCourse executionCourse) {

            List infoCurricularCourses =
                    (List) CollectionUtils.collect(executionCourse.getAssociatedCurricularCourses(), new Transformer() {

                        @Override
                        public Object transform(Object input) {
                            CurricularCourse curricularCourse = (CurricularCourse) input;
                            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
                            if (degreeType.equals(DegreeType.DEGREE)) {
                                detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
                            }
                            return infoCurricularCourse;
                        }
                    });
            return infoCurricularCourses;
        }
    }

    public class NotFoundTeacher extends FenixServiceException {

    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships, new Professorships2DetailProfessorship());

        return detailedProfessorshipList;
    }

    protected Teacher readTeacher(String teacherId) throws NotFoundTeacher {
        final Teacher teacher = FenixFramework.getDomainObject(teacherId);
        if (teacher == null) {
            throw new NotFoundTeacher();
        }
        return teacher;
    }
}