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
 * Created on 2005/02/18
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteEnrollment;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadStudentCurricularInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferEnrollments;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPeopleApp;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz
 */
@StrutsFunctionality(app = ManagerPeopleApp.class, path = "manage-students", titleKey = "link.manager.studentsManagement")
@Mapping(module = "manager", path = "/studentsManagement", input = "/studentsManagement.do?method=show&page=0",
        formBean = "studentCurricularPlanForm")
@Forwards({ @Forward(name = "createStudentCurricularPlan", path = "/manager/createStudentCurricularPlan.jsp"),
        @Forward(name = "transferEnrollments", path = "/manager/transferEnrollments.jsp"),
        @Forward(name = "show", path = "/manager/studentCurricularPlan.jsp") })
@Exceptions(value = {
        @ExceptionHandling(type = NonExistingServiceException.class, key = "exception.student.does.not.exist",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = ExistingServiceException.class, key = "student.curricular.plan.already.exists",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = DomainException.class, key = "error.enrolmentEvaluation.cannot.be.deleted",
                handler = FenixErrorExceptionHandler.class, scope = "request") })
public class ManageStudentCurricularPlanDA extends FenixDispatchAction {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @EntryPoint
    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String studentNumberString = (String) dynaActionForm.get("number");
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");

        if (isPresent(studentNumberString) && isPresent(degreeTypeString)) {
            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);
            putStudentCurricularInformationInRequest(request, Integer.valueOf(studentNumberString), degreeType);
        }

        return mapping.findForward("show");
    }

    public ActionForward deleteStudentCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");

        DeleteStudentCurricularPlan.run(studentCurricularPlanIdString);

        return show(mapping, form, request, response);
    }

    public ActionForward deleteEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String enrollmentIdString = request.getParameter("enrollmentId");
        final String studentNumberString = request.getParameter("studentNumber");
        final String degreeTypeString = request.getParameter("degreeType");
        final Integer studentNumber = Integer.valueOf(studentNumberString);
        final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);

        final User userView = Authenticate.getUser();

        DeleteEnrollment.run(studentNumber, degreeType, enrollmentIdString);

        return show(mapping, form, request, response);
    }

    public ActionForward prepareCreateStudentCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");

        if (isPresent(degreeTypeString)) {
            // putStudentCurricularPlanStateLabelListInRequest(request);

            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);

            final List infoDegreeCurricularPlans = ReadDegreeCurricularPlansByDegreeType.run(degreeType);

            putDegreeCurricularPlansInRequest(request, infoDegreeCurricularPlans);
        }

        return mapping.findForward("createStudentCurricularPlan");
    }

    public ActionForward createStudentCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String studentNumberString = (String) dynaActionForm.get("number");
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");
        final String studentCurricularPlanStateString = (String) dynaActionForm.get("studentCurricularPlanState");
        final String degreeCurricularPlanIdString = (String) dynaActionForm.get("degreeCurricularPlanId");
        final String startDateString = (String) dynaActionForm.get("startDate");

        if (isPresent(studentNumberString) && isPresent(degreeTypeString) && isPresent(studentCurricularPlanStateString)
                && isPresent(degreeCurricularPlanIdString) && isPresent(startDateString)) {

            final Integer studentNumber = new Integer(studentNumberString);
            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);
            final StudentCurricularPlanState studentCurricularPlanState =
                    StudentCurricularPlanState.valueOf(studentCurricularPlanStateString);
            final Date startDate = simpleDateFormat.parse(startDateString);

            final User userView = Authenticate.getUser();

            CreateStudentCurricularPlan.run(studentNumber, degreeType, studentCurricularPlanState, degreeCurricularPlanIdString,
                    startDate);
        }

        return show(mapping, form, request, response);
    }

    public ActionForward prepareTransferEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        show(mapping, form, request, response);
        return mapping.findForward("transferEnrollments");
    }

    public ActionForward transferEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String selectedStudentCurricularPlanIdString = (String) dynaActionForm.get("selectedStudentCurricularPlanId");
        final String selectedCurriculumGroupID = (String) dynaActionForm.get("selectedCurriculumGroupID");
        final String[] enrollmentStringIDsToTransfer = (String[]) dynaActionForm.get("enrollmentIDsToTransfer");

        if (isPresent(selectedStudentCurricularPlanIdString) && enrollmentStringIDsToTransfer != null
                && enrollmentStringIDsToTransfer.length > 0) {

            final User userView = Authenticate.getUser();

            TransferEnrollments.run(selectedStudentCurricularPlanIdString, enrollmentStringIDsToTransfer,
                    selectedCurriculumGroupID);
        }

        return show(mapping, form, request, response);
    }

    protected void putStudentCurricularInformationInRequest(final HttpServletRequest request, final Integer studentNumber,
            final DegreeType degreeType) throws FenixServiceException {
        final User userView = Authenticate.getUser();

        final List infoStudentCurricularPlans = ReadStudentCurricularInformation.run(studentNumber, degreeType);
        request.setAttribute("infoStudentCurricularPlans", infoStudentCurricularPlans);
    }

    protected boolean isPresent(final String string) {
        return string != null && string.length() > 0;
    }

    protected void putDegreeCurricularPlansInRequest(final HttpServletRequest request, final List infoDegreeCurricularPlans) {
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
        comparatorChain.addComparator(new BeanComparator("initialDate"));
        Collections.sort(infoDegreeCurricularPlans, comparatorChain);
        request.setAttribute("degreeCurricularPlans", infoDegreeCurricularPlans);
    }

}