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
 * Created on 5/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteClasses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class PublicSiteComponentBuilder {

    private static PublicSiteComponentBuilder instance = null;

    public PublicSiteComponentBuilder() {
    }

    public static PublicSiteComponentBuilder getInstance() {
        if (instance == null) {
            instance = new PublicSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, SchoolClass domainClass) throws FenixServiceException {

        if (component instanceof InfoSiteTimetable) {
            return getInfoSiteTimetable((InfoSiteTimetable) component, domainClass);
        } else if (component instanceof InfoSiteClasses) {
            return getInfoSiteClasses((InfoSiteClasses) component, domainClass);
        }

        return null;

    }

    /**
     * @param classes
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteClasses(InfoSiteClasses component, SchoolClass domainClass) throws FenixServiceException {
        ExecutionSemester executionSemester = domainClass.getExecutionPeriod();
        ExecutionDegree executionDegree = domainClass.getExecutionDegree();

        Set<SchoolClass> classes =
                executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionSemester,
                        domainClass.getAnoCurricular());
        List infoClasses = new ArrayList();
        for (final SchoolClass schoolClass : classes) {
            infoClasses.add(copyClass2InfoClass(schoolClass));
        }

        component.setClasses(infoClasses);
        return component;
    }

    /**
     * @param timetable
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, SchoolClass domainClass)
            throws FenixServiceException {
        List infoLessonList = null;

        Collection<Shift> shiftList = domainClass.getAssociatedShifts();
        infoLessonList = new ArrayList();

        ExecutionSemester executionSemester = domainClass.getExecutionPeriod();
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionSemester);

        for (Object element : shiftList) {
            Shift shift = (Shift) element;
            infoLessonList.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }
        component.setInfoExecutionPeriod(infoExecutionPeriod);

        component.setLessons(infoLessonList);

        return component;
    }

    private InfoExecutionCourseEditor copyIExecutionCourse2InfoExecutionCourse(ExecutionCourse executionCourse) {
        InfoExecutionCourseEditor infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseEditor();
            infoExecutionCourse.setExternalId(executionCourse.getExternalId());
            infoExecutionCourse.setNome(executionCourse.getNome());
            infoExecutionCourse.setSigla(executionCourse.getSigla());
            infoExecutionCourse.setInfoExecutionPeriod(copyIExecutionPeriod2InfoExecutionPeriod(executionCourse
                    .getExecutionPeriod()));
        }
        return infoExecutionCourse;
    }

    /**
     * @param shift
     * @return
     */
    private InfoShift copyIShift2InfoShift(Shift shift) {
        InfoShift infoShift = null;
        if (shift != null) {
            infoShift = new InfoShift(shift);
        }
        return infoShift;
    }

    /**
     * @param taux
     * @return
     */
    private InfoClass copyClass2InfoClass(SchoolClass taux) {
        InfoClass infoClass = null;
        if (taux != null) {
            infoClass = new InfoClass(taux);
        }
        return infoClass;
    }

    /**
     * @param period
     * @return
     */
    private InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(ExecutionSemester executionSemester) {
        return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}