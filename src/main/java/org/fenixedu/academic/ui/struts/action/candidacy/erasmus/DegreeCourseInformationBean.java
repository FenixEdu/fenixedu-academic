/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCourseInformationBean implements java.io.Serializable, DataProvider {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Degree chosenDegree;
    CurricularCourse chosenCourse;
    ExecutionYear executionYear;
    MobilityApplicationProcess mobilityApplicationProcess;

    public DegreeCourseInformationBean(final ExecutionYear executionYear,
            final MobilityApplicationProcess mobilityApplicationProcess) {
        setExecutionYear(executionYear);
        setMobilityApplicationProcess(mobilityApplicationProcess);
    }

    public DegreeCourseInformationBean() {
    }

    public Degree getChosenDegree() {
        return chosenDegree;
    }

    public void setChosenDegree(Degree chosenDegree) {
        this.chosenDegree = chosenDegree;
    }

    public CurricularCourse getChosenCourse() {
        return chosenCourse;
    }

    public void setChosenCourse(CurricularCourse chosenCourse) {
        this.chosenCourse = chosenCourse;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    private List<DegreeCurricularPlan> getChosenDegreeCurricularPlans() {
        if (getChosenDegree() != null) {
            return getChosenDegree().getDegreeCurricularPlansForYear(getExecutionYear());
        }

        return new ArrayList<DegreeCurricularPlan>();
    }

    private SortedSet<CurricularCourse> getCurricularCoursesForChosenDegree() {
        final SortedSet<CurricularCourse> result = new TreeSet<CurricularCourse>(CurricularCourse.COMPARATOR_BY_NAME);

        for (DegreeCurricularPlan degreeCurricularPlan : getChosenDegreeCurricularPlans()) {
            ExecutionSemester firstSemester = getExecutionYear().getExecutionSemesterFor(1);
            ExecutionSemester secondSemester = getExecutionYear().getExecutionSemesterFor(2);
            if (getMobilityApplicationProcess().getForSemester().equals(ErasmusApplyForSemesterType.FIRST_SEMESTER)) {
                result.addAll(degreeCurricularPlan.getActiveCurricularCourses(firstSemester));
            }
            result.addAll(degreeCurricularPlan.getActiveCurricularCourses(secondSemester));
        }

        return result;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final DegreeCourseInformationBean chooseDegreeBean = (DegreeCourseInformationBean) source;

        return chooseDegreeBean.getCurricularCoursesForChosenDegree();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    public MobilityApplicationProcess getMobilityApplicationProcess() {
        return mobilityApplicationProcess;
    }

    public void setMobilityApplicationProcess(MobilityApplicationProcess mobilityApplicationProcess) {
        this.mobilityApplicationProcess = mobilityApplicationProcess;
    }
}