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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.ReadExecutionCourseWithShiftsAndCurricularCoursesByOID;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.ReadInfoExecutionCourseByOID;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.SeperateExecutionCourse;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.action.utils.RequestUtils;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "academicAdministration", path = "/seperateExecutionCourse",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&page=0",
        formBean = "separateExecutionCourseForm", functionality = EditExecutionCourseDA.class)
@Forwards(value = {
        @Forward(name = "returnFromTransfer",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp"),
        @Forward(name = "manageCurricularSeparation",
                path = "/academicAdministration/executionCourseManagement/manageCurricularSeparation.jsp"),
        @Forward(name = "showTransferPage",
                path = "/academicAdministration/executionCourseManagement/transferCurricularCourses.jsp"),
        @Forward(name = "showSeparationPage",
                path = "/academicAdministration/executionCourseManagement/separateExecutionCourse.jsp") })
public class SeperateExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId"); // maybe not needed (have EC id)

        CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
        request.setAttribute("curYear", curYear.getYear().toString());

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
        request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(executionCourseId);
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        List executionDegrees =
                ReadExecutionDegreesByExecutionPeriodId.runForAcademicAdminAdv(infoExecutionCourse.getInfoExecutionPeriod()
                        .getExternalId());
        transformExecutionDegreesIntoLabelValueBean(executionDegrees);
        request.setAttribute("executionDegrees", executionDegrees);

        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
        return mapping.findForward("showTransferPage");
    }

    public ActionForward prepareSeparate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");

        CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
        request.setAttribute("curYear", curYear.getYear().toString());

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
        request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(executionCourseId);
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        return mapping.findForward("showSeparationPage");
    }

    public ActionForward manageCurricularSeparation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        // FIXME:  ugly code to get attribute before parameter (parameter needs to be changed when coming from separate)
        String executionCourseId = (String) request.getAttribute("executionCourseId");
        if (executionCourseId == null) {
            executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        }

        InfoExecutionCourse infoExecutionCourse;

        try {
            infoExecutionCourse = ReadInfoExecutionCourseByOID.run(executionCourseId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(), new BeanComparator("name"));
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);

        // Setting bean for return to listExecutionCourseActions
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        Boolean chooseNotLinked = false;
        if (!StringUtils.isEmpty(executionCoursesNotLinked) && Boolean.valueOf(executionCoursesNotLinked)) {
            chooseNotLinked = true;
        }

        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        ExecutionInterval executionPeriod = FenixFramework.getDomainObject(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();
        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
            request.setAttribute("curYear", curYear.getYear().toString());
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());
        }

        request.setAttribute("sessionBean", sessionBean);

        return mapping.findForward("manageCurricularSeparation");
    }

    private void transformExecutionDegreesIntoLabelValueBean(List executionDegreeList) {
        CollectionUtils.transform(executionDegreeList, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
                /*
                TODO: DUPLICATE check really needed?
                StringBuilder label =
                        new StringBuilder(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType()
                                .getLocalizedName());
                label.append(" em ");
                label.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
                */

                String label =
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                                .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());

                return new LabelValueBean(label, infoExecutionDegree.getExternalId().toString());
            }

        });

        Collections.sort(executionDegreeList, new BeanComparator("label"));
        executionDegreeList.add(0, new LabelValueBean(BundleUtil.getString(Bundle.RENDERER, "renderers.menu.default.title"), ""));
    }

    public ActionForward changeDestinationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        prepareTransfer(mapping, form, request, response);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String destinationExecutionDegreeId = (String) dynaActionForm.get("destinationExecutionDegreeId");
        String destinationCurricularYear = (String) dynaActionForm.get("destinationCurricularYear");

        if (isSet(destinationExecutionDegreeId) && isSet(destinationCurricularYear)) {

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute("infoExecutionCourse");

            List executionCourses =
                    ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(destinationExecutionDegreeId,
                            infoExecutionCourse.getInfoExecutionPeriod().getExternalId(), new Integer(destinationCurricularYear));
            executionCourses.remove(infoExecutionCourse);
            Collections.sort(executionCourses, new BeanComparator("nome"));
            request.setAttribute("executionCourses", executionCourses);
        }

        return mapping.findForward("showTransferPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixActionException {
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String destinationExecutionCourseIdString = (String) dynaActionForm.get("destinationExecutionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        String curricularYearId = (String) dynaActionForm.get("curricularYearId");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");
        ExecutionDegree originExecutionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
        ExecutionCourse originExecutionCourse = FenixFramework.getDomainObject(executionCourseId);
        String originExecutionDegreesString = originExecutionCourse.getDegreePresentationString();
        String destinationExecutionCourseId = null;

        try {

            if (!StringUtils.isEmpty(destinationExecutionCourseIdString)
                    && StringUtils.isNumeric(destinationExecutionCourseIdString)) {
                destinationExecutionCourseId = destinationExecutionCourseIdString;
            } else {
                throw new DomainException("error.selection.noDestinationExecutionCourse");
            }

            ExecutionCourse destinationExecutionCourse =
                    SeperateExecutionCourse.run(executionCourseId, destinationExecutionCourseId, shiftIdsToTransfer,
                            curricularCourseIdsToTransfer);

            String destinationExecutionCourseName = destinationExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(destinationExecutionCourseName)) {
                destinationExecutionCourseName = destinationExecutionCourse.getName();
            }
            String destinationExecutionCourseCode = destinationExecutionCourse.getSigla();
            String destinationDegreeName = destinationExecutionCourse.getDegreePresentationString();
            String transferedCurricularCourses = makeObjectStringFromArray(curricularCourseIdsToTransfer, CurricularCourse.class);
            String transferedShifts;

            String successKey;
            if (shiftIdsToTransfer.length == 0) {
                successKey = "message.manager.executionCourseManagement.transferCourse.success.many.noShifts";
                transferedShifts = "";
            } else {
                successKey = "message.manager.executionCourseManagement.transferCourse.success.many";
                transferedShifts = makeObjectStringFromArray(shiftIdsToTransfer, Shift.class);
            }
            addActionMessage("success", request, successKey, transferedCurricularCourses, transferedShifts,
                    destinationExecutionCourseName, destinationDegreeName, destinationExecutionCourseCode);

            // check if degree context has changed
            if (!originExecutionCourse.getExecutionDegrees().contains(originExecutionDegree)) {
                // origin execution course degree has changed (no longer on original degree)
                String originCourseName = originExecutionCourse.getNameI18N().getContent();
                if (StringUtils.isEmpty(originCourseName)) {
                    originCourseName = originExecutionCourse.getName();
                }
                addActionMessage("info", request,
                        "message.manager.executionCourseManagement.transferCourse.success.switchContext", originCourseName,
                        originExecutionDegreesString, originExecutionCourse.getDegreePresentationString(),
                        destinationExecutionCourseName, destinationExecutionCourse.getDegreePresentationString(),
                        originExecutionDegree.getDegree().getSigla());
                request.setAttribute("executionCourseId", destinationExecutionCourse.getExternalId().toString());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            if (request.getAttribute("destinationExecutionDegreeId") != null) {
                request.setAttribute("destinationExecutionDegreeId", request.getAttribute("destinationExecutionDegreeId"));
            }
            if (curricularYearId != null) {
                request.setAttribute("destinationCurricularYear", curricularYearId);
            }
            if (request.getAttribute("executionCourses") != null) {
                request.setAttribute("executionCourses", request.getAttribute("executionCourses"));
            }
            if (destinationExecutionCourseId != null) {
                request.setAttribute("destinationExecutionCourseId", destinationExecutionCourseId.toString());
            }
            return changeDestinationContext(mapping, dynaActionForm, request, response);
        }

        return manageCurricularSeparation(mapping, dynaActionForm, request, response);
    }

    public ActionForward separate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixActionException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");
        ExecutionDegree originExecutionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
        ExecutionCourse originExecutionCourse = FenixFramework.getDomainObject(executionCourseId);
        String originExecutionDegreesString = originExecutionCourse.getDegreePresentationString();

        try {

            ExecutionCourse destinationExecutionCourse =
                    SeperateExecutionCourse.run(executionCourseId, null, shiftIdsToTransfer, curricularCourseIdsToTransfer);

            String destinationExecutionCourseName = destinationExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(destinationExecutionCourseName)) {
                destinationExecutionCourseName = destinationExecutionCourse.getName();
            }
            String destinationExecutionCourseCode = destinationExecutionCourse.getSigla();
            String destinationDegreeName = destinationExecutionCourse.getDegreePresentationString();
            String transferedCurricularCourses = makeObjectStringFromArray(curricularCourseIdsToTransfer, CurricularCourse.class);
            String transferedShifts;

            String successKey;
            if (shiftIdsToTransfer.length == 0) {
                successKey = "message.manager.executionCourseManagement.separate.success.create.noShifts";
                transferedShifts = "";
            } else {
                successKey = "message.manager.executionCourseManagement.separate.success.create";
                transferedShifts = makeObjectStringFromArray(shiftIdsToTransfer, Shift.class);
            }
            addActionMessage("success", request, successKey, destinationExecutionCourseName, destinationDegreeName,
                    destinationExecutionCourseCode, transferedCurricularCourses, transferedShifts);

            // check if degree context has changed
            if (!originExecutionCourse.getExecutionDegrees().contains(originExecutionDegree)) {
                // origin execution course degree has changed (no longer on original degree)
                String originCourseName = originExecutionCourse.getNameI18N().getContent();
                if (StringUtils.isEmpty(originCourseName)) {
                    originCourseName = originExecutionCourse.getName();
                }
                addActionMessage("info", request, "message.manager.executionCourseManagement.separate.success.switchContext",
                        originCourseName, originExecutionDegreesString, originExecutionCourse.getDegreePresentationString(),
                        destinationExecutionCourseName, destinationExecutionCourse.getDegreePresentationString(),
                        originExecutionDegree.getDegree().getSigla());
                request.setAttribute("executionCourseId", destinationExecutionCourse.getExternalId().toString());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return prepareSeparate(mapping, dynaActionForm, request, response);
        }

        return manageCurricularSeparation(mapping, dynaActionForm, request, response); //mapping.findForward("manageCurricularSeparation");
    }

    private boolean isSet(String parameter) {
        return !StringUtils.isEmpty(parameter) && StringUtils.isNumeric(parameter);
    }

    private String makeObjectStringFromArray(String[] ids, Class objectType) {

        StringBuilder sb = new StringBuilder();

        if (objectType.equals(CurricularCourse.class)) {
            for (String id : ids) {
                sb.append(curricularCourseToString(id));
                sb.append(", ");
            }
        } else if (objectType.equals(Shift.class)) {
            for (String id : ids) {
                sb.append(shiftToString(id));
                sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // trim ", "
        } else {
            sb.append(BundleUtil.getString(Bundle.APPLICATION, "label.empty"));
        }
        return sb.toString();
    }

    private String curricularCourseToString(String id) {
        CurricularCourse curricularCourse = FenixFramework.getDomainObject(id);
        String name = curricularCourse.getNameI18N().getContent();
        if (StringUtils.isEmpty(name)) {
            name = curricularCourse.getName();
        }
        return name + " [" + curricularCourse.getDegree().getSigla() + "]";
    }

    private String shiftToString(String id) {
        Shift shift = FenixFramework.getDomainObject(id);
        return shift.getPresentationName();
    }

}
