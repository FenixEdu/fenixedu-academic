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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoCurricularYear;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.resourceAllocationManager.ApagarTurma;
import org.fenixedu.academic.service.services.resourceAllocationManager.CriarTurma;
import org.fenixedu.academic.service.services.resourceAllocationManager.DeleteClasses;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.action.utils.ContextUtils;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/manageClasses", module = "resourceAllocationManager", formBean = "classForm",
        input = "/manageClasses.do?method=listClasses", functionality = ExecutionPeriodDA.class)
@Forwards(@Forward(name = "ShowClassList", path = "/resourceAllocationManager/manageClasses_bd.jsp"))
@Exceptions(@ExceptionHandling(handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException", scope = "request"))
public class ManageClassesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());

        final Set<SchoolClass> classes;
        Integer curricularYear = infoCurricularYear.getYear();
        if (curricularYear != null) {
            classes = executionDegree.findSchoolClassesByAcademicIntervalAndCurricularYear(academicInterval, curricularYear);
        } else {
            classes = executionDegree.findSchoolClassesByAcademicInterval(academicInterval);
        }

        final List<InfoClass> infoClassesList = new ArrayList<InfoClass>();

        for (final SchoolClass schoolClass : classes) {
            InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClassesList.add(infoClass);
        }

        if (infoClassesList != null && !infoClassesList.isEmpty()) {
            BeanComparator nameComparator = new BeanComparator("nome");
            Collections.sort(infoClassesList, nameComparator);

            request.setAttribute(PresentationConstants.CLASSES, infoClassesList);
        }
        request.setAttribute("executionDegreeD", executionDegree);

        return mapping.findForward("ShowClassList");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaValidatorForm classForm = (DynaValidatorForm) form;
        String className = (String) classForm.get("className");

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));

        Integer curricularYear = infoCurricularYear.getYear();

        try {
            CriarTurma.run(className, curricularYear, infoExecutionDegree, academicInterval);

        } catch (DomainException e) {
            throw new ExistingActionException("A SchoolClass", e);
        }

        return listClasses(mapping, form, request, response);
    }

    /**
     * Delete class.
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ContextUtils.setClassContext(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        ApagarTurma.run(infoClass);

        request.removeAttribute(PresentationConstants.CLASS_VIEW);

        return listClasses(mapping, form, request, response);
    }

    public ActionForward deleteClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm deleteClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) deleteClassesForm.get("selectedItems");

        if (selectedClasses.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        List<String> classOIDs = new ArrayList<String>();
        for (String selectedClasse : selectedClasses) {
            classOIDs.add(selectedClasse);
        }

        DeleteClasses.run(classOIDs);

        return listClasses(mapping, form, request, response);

    }

}