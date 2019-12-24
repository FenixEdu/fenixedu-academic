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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.comparators.ComparatorByNameForInfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.AssociateCurricularCoursesToExecutionCourse;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.DissociateCurricularCourseByExecutionCourseId;
import org.fenixedu.academic.service.services.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
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

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
@Mapping(module = "academicAdministration", path = "/editExecutionCourseManageCurricularCourses",
        input = "/editExecutionCourse.do?method=prepareEditExecutionCourse&page=0", formBean = "executionCourseForm",
        functionality = EditExecutionCourseDA.class)
@Forwards(value = {
        @Forward(name = "editExecutionCourse",
                path = "/academicAdministration/editExecutionCourse.do?method=editExecutionCourse&page=0"),
        @Forward(name = "listExecutionCourseActions",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp"),
        @Forward(name = "manageCurricularSeparation",
                path = "/academicAdministration/seperateExecutionCourse.do?method=manageCurricularSeparation"),
        @Forward(name = "associateCurricularCourse",
                path = "/academicAdministration/executionCourseManagement/associateCurricularCourse.jsp"),
        @Forward(name = "prepareAssociateCurricularCourseChooseDegreeCurricularPlan",
                path = "/academicAdministration/executionCourseManagement/prepareAssociateCurricularCourseChooseDegreeCurricularPlan.jsp") })
public class EditExecutionCourseManageCurricularCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward dissociateCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String curricularCourseId = RequestUtils.getAndSetStringToRequest(request, "curricularCourseId");
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        String executionCourseName = executionCourse.getName() + " [" + executionCourse.getDegreePresentationString() + "]";

        try {
            DissociateCurricularCourseByExecutionCourseId.run(executionCourseId, curricularCourseId);
            CurricularCourse curricularCourse = FenixFramework.getDomainObject(curricularCourseId);
            addActionMessage("success", request, "message.manager.executionCourseManagement.dissociate.success",
                    curricularCourse.getName(), curricularCourse.getDegreeCurricularPlan().getName());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Set<Degree> degrees = executionCourse.getDegreesSortedByDegreeName();
        // destination attributes
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        ExecutionDegree originExecutionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
        request.setAttribute("originExecutionDegreeName", originExecutionDegree.getPresentationName());
        Boolean chooseNotLinked = Boolean.valueOf(RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked"));
        String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");

        if (!degrees.contains(originExecutionDegree.getDegree())) {
            ExecutionCourseBean sessionBean = new ExecutionCourseBean();
            sessionBean.setSourceExecutionCourse(executionCourse);
            sessionBean.setExecutionSemester(executionCourse.getExecutionPeriod());
            sessionBean.setChooseNotLinked(chooseNotLinked);
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
            sessionBean.setExecutionDegree(originExecutionDegree);
            sessionBean.setCurricularYear(curYear);

            request.setAttribute("sessionBean", sessionBean);
            addActionMessage("info", request, "message.manager.executionCourseManagement.dissociate.success.switchContext",
                    executionCourseName);
            return mapping.findForward("listExecutionCourseActions");
        }

        return mapping.findForward("manageCurricularSeparation");
    }

    public ActionForward prepareAssociateCurricularCourseChooseDegreeCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        //TODO: check & clean up attributes that are not needed
        //processing attributes
        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        List<InfoExecutionDegree> executionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {
            executionDegreeList = ReadExecutionDegreesByExecutionPeriodId.runForAcademicAdmin(executionPeriodId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> degrees = new ArrayList<LabelValueBean>();
        degrees.add(new LabelValueBean(BundleUtil.getString(Bundle.RENDERER, "renderers.menu.default.title"), ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        buildExecutionDegreeLabelValueBean(executionDegreeList, degrees);
        request.setAttribute(PresentationConstants.DEGREES, degrees);

        //destination attributes
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (StringUtils.isEmpty(executionCoursesNotLinked) || !Boolean.valueOf(executionCoursesNotLinked)) {
            String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
            request.setAttribute("curYear", curYear.getYear().toString());
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        }
        RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        RequestUtils.getAndSetStringToRequest(request, "executionCourseName");
        ExecutionInterval executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        request.setAttribute("executionPeriodName", executionSemester.getQualifiedName());
        return mapping.findForward("prepareAssociateCurricularCourseChooseDegreeCurricularPlan");
    }

    public ActionForward prepareAssociateCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        //TODO: check and clean up unneeded attributes
        //informative and destination attributes
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (StringUtils.isEmpty(executionCoursesNotLinked) || !Boolean.valueOf(executionCoursesNotLinked)) {
            String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);
            request.setAttribute("curYear", curYear.getYear().toString());
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(originExecutionDegreeId);
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());
        }
        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        ExecutionInterval executionPeriod = FenixFramework.getDomainObject(executionPeriodId);
        request.setAttribute("executionPeriodName", executionPeriod.getQualifiedName());
        RequestUtils.getAndSetStringToRequest(request, "executionCourseName");

        //FIXME: executionPeriod might not be needed (present in exec course)
        //processing attributes
        String degreeCurricularPlanId = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanId");
        DegreeCurricularPlan degreeCurricularPlan = null;
        if (!StringUtils.isEmpty(degreeCurricularPlanId)) {
            degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        }
        try {
            if (degreeCurricularPlan == null) {
                throw new DomainException("error.selection.noDegree");
            }
            request.setAttribute("degreeCurricularPlanName",
                    degreeCurricularPlan.getPresentationName(executionPeriod.getExecutionYear()));
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
            String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
            final ExecutionInterval executionInterval = executionCourse.getExecutionInterval();
            final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
            for (final DegreeModule degreeModule : rootDomainObject.getDegreeModulesSet()) {
                if (degreeModule instanceof CurricularCourse) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                    if (!executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)
                            && !curricularCourse.hasAnyExecutionCourseIn(executionInterval)) {
                        if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, degreeCurricularPlan,
                                executionInterval)) {
                            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
                        }
                    }
                }
            }
            Collections.sort(infoCurricularCourses, new BeanComparator("name"));
            request.setAttribute("infoCurricularCourses", infoCurricularCourses);

            return mapping.findForward("associateCurricularCourse");

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return prepareAssociateCurricularCourseChooseDegreeCurricularPlan(mapping, form, request, response);
        }
    }

    public ActionForward associateCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodName");
        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");

        Integer curricularCoursesListSize = (Integer) executionCourseForm.get("curricularCoursesListSize");

        List<String> curricularCourseIds =
                getInformationToDissociate(request, curricularCoursesListSize, "curricularCourse", "externalId", "chosen");

        try {
            AssociateCurricularCoursesToExecutionCourse.run(executionCourseId, curricularCourseIds);

            // avmc (ist150958): success messages: 1 line for each curricular course
            String degreeCurricularPlanId = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanId");
            DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

            addActionMessage("success", request, "message.manager.executionCourseManagement.associateCourse.success",
                    degreeCurricularPlan.getName());
            for (final String curricularCourseId : curricularCourseIds) {
                final CurricularCourse curricularCourse = FenixFramework.getDomainObject(curricularCourseId);
                addActionMessage("successCourse", request,
                        "message.manager.executionCourseManagement.associateCourse.success.line", curricularCourse.getName());
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return prepareAssociateCurricularCourse(mapping, executionCourseForm, request, response);
        }

        RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        return mapping.findForward("manageCurricularSeparation");
    }

    protected void buildExecutionDegreeLabelValueBean(List<InfoExecutionDegree> executionDegreeList,
            List<LabelValueBean> courses) {

        for (InfoExecutionDegree infoExecutionDegree : executionDegreeList) {
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                    .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());
            /*
            TODO: DUPLICATE check really needed?
            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().getLocalizedName() + " em " + name;
            
            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
            */
            // courses.add(new LabelValueBean(name, name + "~" + infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId().toString()));
            courses.add(new LabelValueBean(name, infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId()));
        }
    }

    private List<String> getInformationToDissociate(HttpServletRequest request, Integer curricularCoursesListSize, String what,
            String property, String formProperty) {

        List<String> informationToDeleteList = new ArrayList<String>();
        for (int i = 0; i < curricularCoursesListSize.intValue(); i++) {
            String informationToDelete = dataToDelete(request, i, what, property, formProperty);
            if (informationToDelete != null) {
                informationToDeleteList.add(informationToDelete);
            }
        }
        return informationToDeleteList;
    }

    private String dataToDelete(HttpServletRequest request, int index, String what, String property, String formProperty) {

        String itemToDelete = null;
        String checkbox = request.getParameter(what + "[" + index + "]." + formProperty);
        String toDelete = null;
        if (checkbox != null && (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true"))) {
            toDelete = request.getParameter(what + "[" + index + "]." + property);
        }
        if (toDelete != null) {
            itemToDelete = toDelete;
        }
        return itemToDelete;
    }

}
