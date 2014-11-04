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
package org.fenixedu.academic.ui.struts.action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Curriculum;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;

@Mapping(path = "/manageObjectives", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "objectivesForm")
public class ExecutionCourseObjectivesDA extends ManageExecutionCourseDA {

    // OBJECTIVES

    public static class CurriculumFactoryEditCurriculum extends CurricularCourse.CurriculumFactory implements FactoryExecutor {
        private Curriculum curriculum;

        public CurriculumFactoryEditCurriculum(CurricularCourse curricularCourse) {
            super(curricularCourse);
            setLastModification(new DateTime());
            curriculum = null;
        }

        public CurriculumFactoryEditCurriculum(Curriculum curriculum) {
            super(curriculum.getCurricularCourse());
            this.curriculum = curriculum;
        }

        public Curriculum getCurriculum() {
            return curriculum;
        }

        public void setCurriculum(Curriculum curriculum) {
            this.curriculum = curriculum;
            if (curriculum != null) {
                this.setGeneralObjectives(curriculum.getGeneralObjectives());
                this.setGeneralObjectivesEn(curriculum.getGeneralObjectivesEn());
                this.setOperacionalObjectives(curriculum.getOperacionalObjectives());
                this.setOperacionalObjectivesEn(curriculum.getOperacionalObjectivesEn());
                this.setProgram(curriculum.getProgram());
                this.setProgramEn(curriculum.getProgramEn());
            }
        }

        @Override
        public Curriculum execute() {
            final Curriculum curriculum = getCurriculum();
            if (curriculum == null) {
                final CurricularCourse curricularCourse = getCurricularCourse();
                return curricularCourse == null ? null : curricularCourse.editCurriculum(getProgram(), getProgramEn(),
                        getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(),
                        getOperacionalObjectivesEn(), getLastModification());
            } else {
                final DateTime dt = curriculum.getLastModificationDateDateTime();
                curriculum.edit(getGeneralObjectives(), getOperacionalObjectives(), getProgram(), getGeneralObjectivesEn(),
                        getOperacionalObjectivesEn(), getProgramEn());
                curriculum.setLastModificationDateDateTime(dt);
                return curriculum;
            }
        }
    }

    public static class CurriculumFactoryInsertCurriculum extends CurricularCourse.CurriculumFactory implements FactoryExecutor {
        public CurriculumFactoryInsertCurriculum(CurricularCourse curricularCourse, ExecutionCourse executionCourse) {
            super(curricularCourse);
            setLastModification(executionCourse.getExecutionPeriod().getBeginDateYearMonthDay().toDateTimeAtMidnight());
        }

        @Override
        public Curriculum execute() {
            final CurricularCourse curricularCourse = getCurricularCourse();
            return curricularCourse == null ? null : curricularCourse.insertCurriculum(getProgram(), getProgramEn(),
                    getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(), getOperacionalObjectivesEn(),
                    getLastModification());
        }

    }

    @Input
    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return forward(request, "/teacher/executionCourse/objectives.jsp");
    }

    public ActionForward prepareCreateObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareCurricularCourse(request);
        return forward(request, "/teacher/executionCourse/createObjectives.jsp");
    }

    public ActionForward createObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {
        executeFactoryMethod();
        return forward(request, "/teacher/executionCourse/objectives.jsp");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        final Teacher teacher = getUserView(request).getPerson().getTeacher();
        if (teacher.isResponsibleFor(executionCourse) == null) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.teacherNotResponsibleOrNotCoordinator"));
            saveErrors(request, messages);
            return forward(request, "/teacher/executionCourse/objectives.jsp");
        }

        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, curriculumIDString);
            if (curriculum != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("generalObjectives", curriculum.getGeneralObjectives());
                dynaActionForm.set("generalObjectivesEn", curriculum.getGeneralObjectivesEn());
                dynaActionForm.set("operacionalObjectives", curriculum.getOperacionalObjectives());
                dynaActionForm.set("operacionalObjectivesEn", curriculum.getOperacionalObjectivesEn());
            }
            request.setAttribute("curriculum", curriculum);
        }
        return forward(request, "/teacher/executionCourse/editObjectives.jsp");
    }

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        executeFactoryMethod();
        return forward(request, "/teacher/executionCourse/objectives.jsp");
    }

}
