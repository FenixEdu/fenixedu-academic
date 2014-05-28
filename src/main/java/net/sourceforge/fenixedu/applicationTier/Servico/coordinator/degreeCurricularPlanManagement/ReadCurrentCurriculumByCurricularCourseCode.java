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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode {

    @Atomic
    public static InfoCurriculum run(String executionDegreeCode, String curricularCourseCode) throws FenixServiceException {
        check(RolePredicates.COORDINATOR_PREDICATE);

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        // selects active curricular course scopes
        List<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes();

        activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                if (curricularCourseScope.isActive().booleanValue()) {
                    return true;
                }
                return false;
            }
        });

        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

        List<ExecutionCourse> associatedExecutionCourses = new ArrayList<ExecutionCourse>();
        Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                associatedExecutionCourses.add(executionCourse);
            }
        }

        Curriculum curriculum = curricularCourse.findLatestCurriculum();
        InfoCurriculum infoCurriculum = null;
        if (curriculum != null) {
            infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);
        } else {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.setExternalId(null);
            infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        infoCurriculum = createInfoCurriculum(infoCurriculum, activeCurricularCourseScopes, associatedExecutionCourses);
        return infoCurriculum;
    }

    private static InfoCurriculum createInfoCurriculum(InfoCurriculum infoCurriculum, List activeCurricularCourseScopes,
            List associatedExecutionCourses) {

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();
            infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}