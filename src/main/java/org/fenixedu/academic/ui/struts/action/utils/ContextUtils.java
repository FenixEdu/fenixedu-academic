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
/*
 * Created on 2003/07/28
 *
 */
package org.fenixedu.academic.ui.struts.action.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoCurricularYear;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.dto.resourceAllocationManager.ContextSelectionBean;
import org.fenixedu.academic.service.services.commons.ReadCurricularYearByOID;
import org.fenixedu.academic.service.services.commons.ReadExecutionCourseByOID;
import org.fenixedu.academic.service.services.commons.ReadExecutionDegreeByOID;
import org.fenixedu.academic.service.services.commons.ReadExecutionPeriodByOID;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadClassByOID;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadLessonByOID;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadShiftByOID;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz &amp; Sara Ribeiro
 *
 */
public class ContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(ContextUtils.class);

    @Deprecated
    public static final void setExecutionPeriodContext(HttpServletRequest request) throws FenixServiceException {
        String executionPeriodOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID);
        if (executionPeriodOIDString == null) {
            executionPeriodOIDString = request.getParameter(PresentationConstants.EXECUTION_PERIOD_OID);
        }

        String executionPeriodOID = null;
        if (executionPeriodOIDString != null && !executionPeriodOIDString.equals("")
                && !executionPeriodOIDString.equals("null")) {
            executionPeriodOID = executionPeriodOIDString;
        }

        InfoExecutionPeriod infoExecutionPeriod = null;
        if (executionPeriodOID != null) {
            infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodOID);
        } else {
            infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(ExecutionInterval.findFirstCurrentChild(null));
        }
        if (infoExecutionPeriod != null) {
            // Place it in request
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());
            if (infoExecutionPeriod.getInfoExecutionYear() != null) {
                request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
            }
        }
    }

    /**
     * @param request
     */
    public static void setExecutionDegreeContext(HttpServletRequest request) throws FenixServiceException {

        String executionDegreeOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID);

        if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
            executionDegreeOIDString = request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID);

            // No degree was chosen
            if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
                request.setAttribute(PresentationConstants.EXECUTION_DEGREE, null);
            }
        }

        InfoExecutionDegree infoExecutionDegree = null;

        if (executionDegreeOIDString != null) {
            infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeOIDString);

            if (infoExecutionDegree != null) {
                // Place it in request
                request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getExternalId().toString());
            }
        }
    }

    /**
     * @param request
     */
    public static void setCurricularYearContext(HttpServletRequest request) {
        String curricularYearOIDString = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID);
        if (curricularYearOIDString == null) {
            curricularYearOIDString = request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID);
        }

        String curricularYearOID = null;
        if (curricularYearOIDString != null && !curricularYearOIDString.equals("null")) {
            curricularYearOID = curricularYearOIDString;
        }

        InfoCurricularYear infoCurricularYear = null;

        if (curricularYearOID != null) {
            // Read from database
            try {

                infoCurricularYear = ReadCurricularYearByOID.run(curricularYearOID);
            } catch (FenixServiceException e) {
                logger.error(e.getMessage(), e);
            }

            if (infoCurricularYear != null) {
                // Place it in request
                request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);
                request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, infoCurricularYear.getExternalId().toString());
            }

        }
    }

    /**
     * @param request
     */
    public static void setExecutionCourseContext(HttpServletRequest request) {
        String executionCourseOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_COURSE_OID);
        if (executionCourseOIDString == null) {
            executionCourseOIDString = request.getParameter(PresentationConstants.EXECUTION_COURSE_OID);
        }

        String executionCourseOID = null;
        if (executionCourseOIDString != null && !executionCourseOIDString.equals("")
                && !executionCourseOIDString.equals("null")) {
            executionCourseOID = executionCourseOIDString;
        }

        InfoExecutionCourse infoExecutionCourse = null;

        if (executionCourseOID != null) {
            infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourseOID);

            if (infoExecutionCourse != null) {
                // Place it in request
                request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
            }
        }
    }

    /**
     * @param request
     */
    public static void setShiftContext(HttpServletRequest request) {
        String shiftOIDString = (String) request.getAttribute(PresentationConstants.SHIFT_OID);
        if (shiftOIDString == null) {
            shiftOIDString = request.getParameter(PresentationConstants.SHIFT_OID);
        }

        InfoShift infoShift = null;

        if (shiftOIDString != null) {
            infoShift = ReadShiftByOID.run(shiftOIDString);

            if (infoShift != null) {
                // Place it in request
                request.setAttribute(PresentationConstants.SHIFT, infoShift);
            }
        }
    }

    /**
     * @param request
     */
    public static void setClassContext(HttpServletRequest request) {
        String classOIDString = (String) request.getAttribute(PresentationConstants.CLASS_VIEW_OID);
        if (classOIDString == null) {
            classOIDString = request.getParameter(PresentationConstants.CLASS_VIEW_OID);
        }

        InfoClass infoClass = null;

        if (classOIDString != null) {
            // Read from database
            try {

                infoClass = ReadClassByOID.run(classOIDString);
            } catch (FenixServiceException e) {
                logger.error(e.getMessage(), e);
            }

            // Place it in request
            request.setAttribute(PresentationConstants.CLASS_VIEW, infoClass);
        }
    }

    /**
     * @param request
     */
    public static void setLessonContext(HttpServletRequest request) {
        String lessonOIDString = (String) request.getAttribute(PresentationConstants.LESSON_OID);
        if (lessonOIDString == null) {
            lessonOIDString = request.getParameter(PresentationConstants.LESSON_OID);
        }

        InfoLesson infoLesson = null;

        if (lessonOIDString != null) {
            infoLesson = ReadLessonByOID.run(lessonOIDString);

            // Place it in request
            request.setAttribute(PresentationConstants.LESSON, infoLesson);
        }
    }

    @Deprecated
    public static void prepareChangeExecutionDegreeAndCurricularYear(HttpServletRequest request) {

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = getLabelListOfCurricularYears();
        request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        /* Obtain a list of degrees for the specified execution year */
        final ExecutionYear executionYear = infoExecutionPeriod.getExecutionPeriod().getExecutionYear();
        final Set<ExecutionDegree> executionDegrees = executionYear.getExecutionDegreesSet();

        final List<LabelValueBean> labelListOfExecutionDegrees = new ArrayList<LabelValueBean>();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            infoExecutionDegrees.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            final String degreeTypeString = degree.getDegreeType().getName().getContent();
            final StringBuilder name = new StringBuilder();
            name.append(degreeTypeString);
            name.append(" ").append(BundleUtil.getString(Bundle.APPLICATION, "label.in")).append(" ");
            name.append(degree.getNameFor(executionDegree.getExecutionYear()).getContent());
            if (duplicateDegreeInList(degree, executionYear)) {
                name.append(" - ");
                name.append(degreeCurricularPlan.getName());
            }
            final LabelValueBean labelValueBean = new LabelValueBean(name.toString(), executionDegree.getExternalId().toString());
            labelListOfExecutionDegrees.add(labelValueBean);
        }
        Collections.sort(labelListOfExecutionDegrees);
        request.setAttribute("licenciaturas", labelListOfExecutionDegrees);
        Collections.sort(infoExecutionDegrees, InfoExecutionDegree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", infoExecutionDegrees);

        final List<ExecutionInterval> executionSemesters = new ArrayList<>(ExecutionInterval.findActiveChilds());
        Collections.sort(executionSemesters);
        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
        for (final ExecutionInterval executionSemester : executionSemesters) {
            infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
            final String name = executionSemester.getName() + " - " + executionSemester.getExecutionYear().getYear();
            final LabelValueBean labelValueBean = new LabelValueBean(name, executionSemester.getExternalId().toString());
            executionPeriodLabelValueBeans.add(labelValueBean);
        }
        request.setAttribute(PresentationConstants.LIST_INFOEXECUTIONPERIOD, infoExecutionPeriods);
        request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodLabelValueBeans);
    }

    private static boolean duplicateDegreeInList(final Degree degree, final ExecutionYear executionYear) {
        boolean foundOne = false;
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionYear == executionDegree.getExecutionYear()) {
                    if (foundOne) {
                        return true;
                    } else {
                        foundOne = true;
                    }
                }
            }
        }
        return false;
    }

    public static List getLabelListOfCurricularYears() {
        List labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("escolher", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
        return labelListOfCurricularYears;
    }

    public static List getLabelListOfOptionalCurricularYears() {
        List labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("todos", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
        return labelListOfCurricularYears;
    }

    public static void setContextSelectionBean(HttpServletRequest request, Object renderedObject) {
        ContextSelectionBean context = null;
        if (renderedObject != null && renderedObject instanceof ContextSelectionBean) {
            RenderUtils.invalidateViewState();
            context = (ContextSelectionBean) renderedObject;
        } else if (request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN) != null) {
            context = (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);
        } else {
            AcademicInterval academicInterval = null;
            ExecutionDegree executionDegree = null;
            CurricularYear curricularYear = null;
            String courseName = null;
            if (request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL) != null) {
                String academicIntervalStr = (String) request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL);
                academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStr);
            } else if (request.getParameter(PresentationConstants.ACADEMIC_INTERVAL) != null) {
                String academicIntervalStr = request.getParameter(PresentationConstants.ACADEMIC_INTERVAL);
                if (academicIntervalStr != null && !academicIntervalStr.equals("null")) {
                    academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStr);
                }
            }
            if (academicInterval == null) {
                academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
            }
            if (request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID) != null) {
                executionDegree =
                        FenixFramework.getDomainObject((String) request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID));
            } else if (request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID) != null) {
                executionDegree =
                        FenixFramework.getDomainObject(request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID));
            }
            if (request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID) != null
                    && !request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID).equals("null")) {
                curricularYear =
                        FenixFramework.getDomainObject((String) request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID));
            } else if (request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID) != null
                    && !request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID).equals("null")) {
                curricularYear = FenixFramework.getDomainObject(request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID));
            }
            if (request.getAttribute("execution_course_name") != null) {
                courseName = (String) request.getAttribute("execution_course_name");
            } else if (request.getParameter("execution_course_name") != null) {
                courseName = request.getParameter("execution_course_name");
            }

            context = new ContextSelectionBean(academicInterval, executionDegree, curricularYear);
            context.setCourseName(courseName);
        }
        request.setAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN, context);
        request.setAttribute(PresentationConstants.ACADEMIC_INTERVAL,
                context.getAcademicInterval().getResumedRepresentationInStringFormat());

        if (context.getExecutionDegree() != null) {
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE, new InfoExecutionDegree(context.getExecutionDegree()));
        }

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR, new InfoCurricularYear(context.getCurricularYear()));
    }

}