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
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gesdis.ReadCourseInformationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gesdis.ReadCourseInformationCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoricWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCourseHistoric {

    protected List run(String executionCourseId) throws FenixServiceException {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        Integer semester = executionCourse.getExecutionPeriod().getSemester();
        Collection<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        return getInfoSiteCoursesHistoric(executionCourse, curricularCourses, semester);
    }

    private List<InfoSiteCourseHistoric> getInfoSiteCoursesHistoric(ExecutionCourse executionCourse,
            Collection<CurricularCourse> curricularCourses, Integer semester) {
        List<InfoSiteCourseHistoric> result = new ArrayList<InfoSiteCourseHistoric>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            result.add(getInfoSiteCourseHistoric(executionCourse.getExecutionPeriod().getExecutionYear(), curricularCourse,
                    semester));
        }

        return result;
    }

    private InfoSiteCourseHistoric getInfoSiteCourseHistoric(final ExecutionYear executionYear,
            CurricularCourse curricularCourse, Integer semester) {
        InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();

        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
        infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

        final Collection<CourseHistoric> courseHistorics = curricularCourse.getAssociatedCourseHistorics();

        // the historic must only show info regarding the years previous to the
        // year chosen by the user
        List<InfoCourseHistoric> infoCourseHistorics = new ArrayList<InfoCourseHistoric>();
        for (CourseHistoric courseHistoric : courseHistorics) {
            ExecutionYear courseHistoricExecutionYear = ExecutionYear.readExecutionYearByName(courseHistoric.getCurricularYear());
            if (courseHistoric.getSemester().equals(semester)
                    && courseHistoricExecutionYear.getBeginDate().before(executionYear.getBeginDate())) {
                infoCourseHistorics.add(InfoCourseHistoricWithInfoCurricularCourse.newInfoFromDomain(courseHistoric));
            }
        }

        Collections.sort(infoCourseHistorics, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                InfoCourseHistoric infoCourseHistoric1 = (InfoCourseHistoric) o1;
                InfoCourseHistoric infoCourseHistoric2 = (InfoCourseHistoric) o2;
                return infoCourseHistoric2.getCurricularYear().compareTo(infoCourseHistoric1.getCurricularYear());
            }
        });

        infoSiteCourseHistoric.setInfoCourseHistorics(infoCourseHistorics);
        return infoSiteCourseHistoric;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCourseHistoric serviceInstance = new ReadCourseHistoric();

    @Atomic
    public static List runReadCourseHistoric(String executionCourseId) throws FenixServiceException, NotAuthorizedException {
        try {
            ReadCourseInformationAuthorizationFilter.instance.execute(executionCourseId);
            return serviceInstance.run(executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ReadCourseInformationCoordinatorAuthorizationFilter.instance.execute(executionCourseId);
                return serviceInstance.run(executionCourseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    GEPAuthorizationFilter.instance.execute();
                    return serviceInstance.run(executionCourseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}