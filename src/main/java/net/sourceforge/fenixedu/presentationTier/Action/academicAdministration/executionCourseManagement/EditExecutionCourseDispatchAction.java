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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteExecutionCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.EditExecutionCourseInfo;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadInfoExecutionCourseByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 19/Dez/2003
 * 
 */
@Mapping(path = "/editExecutionCourse", module = "academicAdministration",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&amp;page=0",
        formBean = "executionCourseForm", functionality = EditExecutionCourseDA.class)
@Forwards({
        @Forward(name = "editExecutionCourse", path = "/academicAdministration/executionCourseManagement/editExecutionCourse.jsp"),
        @Forward(name = "viewExecutionCourse", path = "/academicAdministration/executionCourseManagement/viewExecutionCourse.jsp"),
        @Forward(name = "listExecutionCourseActions",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDispatchAction extends FenixDispatchAction {
    private static final Logger logger = LoggerFactory.getLogger(EditExecutionCourseDispatchAction.class);

    public ActionForward editExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");

        InfoExecutionCourse infoExecutionCourse;
        try {
            infoExecutionCourse = ReadInfoExecutionCourseByOID.run(executionCourseId);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
        fillForm(form, infoExecutionCourse, request);

        List<LabelValueBean> entryPhases = new ArrayList<LabelValueBean>();
        for (EntryPhase entryPhase : EntryPhase.values()) {
            LabelValueBean labelValueBean = new LabelValueBean(entryPhase.getLocalizedName(), entryPhase.getName());
            entryPhases.add(labelValueBean);
        }
        request.setAttribute("entryPhases", entryPhases);

        DynaActionForm executionCourseForm = prepareReturnAttributes(form, request);
        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), executionCourseId);

        return mapping.findForward("editExecutionCourse");
    }

    public ActionForward updateExecutionCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final InfoExecutionCourseEditor infoExecutionCourseEditor = fillInfoExecutionCourseFromForm(actionForm, request);
        InfoExecutionCourse infoExecutionCourse = null;

        try {
            infoExecutionCourse = EditExecutionCourseInfo.run(infoExecutionCourseEditor);

        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        } catch (DomainException ex) {
            addActionMessage("error", request, ex.getMessage(), ex.getArgs());
            return editExecutionCourse(mapping, actionForm, request, response);
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        DynaActionForm executionCourseForm = prepareReturnAttributes(actionForm, request);
        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), executionCourseId);

        return mapping.findForward("viewExecutionCourse");
    }

    public ActionForward deleteExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");

        DynaActionForm executionCourseForm = prepareReturnAttributes(form, request);

        List<String> internalIds = new ArrayList<String>();
        internalIds.add(executionCourseId);

        List<String> errorCodes = new ArrayList<String>();

        ExecutionCourse executionCourseToBeDeleted = FenixFramework.getDomainObject(executionCourseId);
        String executionCourseName = executionCourseToBeDeleted.getNome();
        String executionCourseSigla = executionCourseToBeDeleted.getSigla();

        try {
            errorCodes = DeleteExecutionCourses.run(internalIds);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        if (!errorCodes.isEmpty()) {
            for (String errorCode : errorCodes) {
                addActionMessage("error", request, "errors.invalid.delete.not.empty.execution.course", errorCode);
            }
        } else {
            addActionMessage("success", request, "message.manager.executionCourseManagement.deleteExecutionCourse.success",
                    executionCourseName, executionCourseSigla);
        }

        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), null);

        return mapping.findForward("listExecutionCourseActions");
    }

    private void fillForm(ActionForm form, InfoExecutionCourse infoExecutionCourse, HttpServletRequest request) {

        DynaActionForm executionCourseForm = (DynaActionForm) form;
        executionCourseForm.set("name", infoExecutionCourse.getNome());
        executionCourseForm.set("code", infoExecutionCourse.getSigla());
        executionCourseForm.set("comment", infoExecutionCourse.getComment());
        executionCourseForm.set("entryPhase", infoExecutionCourse.getEntryPhase().getName());
        if (infoExecutionCourse.getAvailableGradeSubmission() != null) {
            executionCourseForm.set("availableGradeSubmission", infoExecutionCourse.getAvailableGradeSubmission().toString());
        }
        request.setAttribute("courseLoadBean", new CourseLoadBean(infoExecutionCourse.getExecutionCourse()));
    }

    protected DynaActionForm prepareReturnAttributes(ActionForm form, HttpServletRequest request) {
        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        Boolean chooseNotLinked = null;
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {

            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");
            separateLabel(form, request, "curYear", "curYearId", "curYearName");
            String curYear = (String) request.getAttribute("curYear");
            executionCourseForm.set("curYear", curYear);
            chooseNotLinked = new Boolean(false);
        } else {
            chooseNotLinked = new Boolean(executionCoursesNotLinked);
            executionCourseForm.set("executionCoursesNotLinked", chooseNotLinked);
        }
        return executionCourseForm;
    }

    protected void prepareReturnSessionBean(HttpServletRequest request, Boolean chooseNotLinked, String executionCourseId) {
        // Preparing sessionBean for the EditExecutionCourseDA.list...Actions...
        String executionPeriodId = (String) request.getAttribute("executionPeriodId");

        ExecutionCourse executionCourse = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(executionCourseId)) {
            executionCourse = FenixFramework.getDomainObject(executionCourseId);
        }
        ExecutionSemester executionPeriod = FenixFramework.getDomainObject(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();

        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            String executionDegreeId = (String) request.getAttribute("executionDegreeId");
            String curricularYearId = (String) request.getAttribute("curYearId");

            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);

            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
        }

        request.setAttribute("sessionBean", sessionBean);
    }

    private InfoExecutionCourseEditor fillInfoExecutionCourseFromForm(ActionForm actionForm, HttpServletRequest request) {

        InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

        DynaActionForm editExecutionCourseForm = (DynaActionForm) actionForm;

        try {
            infoExecutionCourse.setExternalId((String) editExecutionCourseForm.get("executionCourseId"));
            infoExecutionCourse.setNome((String) editExecutionCourseForm.get("name"));
            infoExecutionCourse.setSigla((String) editExecutionCourseForm.get("code"));
            infoExecutionCourse.setComment((String) editExecutionCourseForm.get("comment"));
            infoExecutionCourse.setAvailableGradeSubmission(Boolean.valueOf(editExecutionCourseForm
                    .getString("availableGradeSubmission")));
            infoExecutionCourse.setEntryPhase(EntryPhase.valueOf(editExecutionCourseForm.getString("entryPhase")));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return infoExecutionCourse;
    }

    protected String separateLabel(ActionForm form, HttpServletRequest request, String property, String id, String name) {

        DynaActionForm executionCourseForm = (DynaActionForm) form;

        // the value returned to action is a string name~externalId
        String object = (String) executionCourseForm.get(property);
        if (object == null || object.length() <= 0) {
            object = (String) request.getAttribute(property);
            if (object == null) {
                object = request.getParameter(property);
            }
        }

        String objectId = null;
        String objectName = null;
        if (object != null && object.length() > 0 && object.indexOf("~") > 0) {
            executionCourseForm.set(property, object);
            request.setAttribute(property, object);

            objectId = StringUtils.substringAfter(object, "~");
            objectName = object.substring(0, object.indexOf("~"));

            request.setAttribute(name, objectName);
            request.setAttribute(id, objectId);
        }

        return objectId;
    }
}