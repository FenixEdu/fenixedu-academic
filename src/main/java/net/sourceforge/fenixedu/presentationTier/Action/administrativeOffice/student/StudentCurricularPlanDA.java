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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/addNewStudentCurricularPlan", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showCreateNewStudentCurricularPlan", path = "/academicAdminOffice/createStudentCurricularPlan.jsp"),
        @Forward(name = "viewRegistrationDetails", path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp") })
public class StudentCurricularPlanDA extends FenixDispatchAction {

    public ActionForward prepareCreateSCP(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getDomainObject(request, "registrationId");

        request.setAttribute("studentCurricularPlanCreator",
                new StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator(registration));
        request.setAttribute("registration", registration);

        return mapping.findForward("showCreateNewStudentCurricularPlan");
    }

    public ActionForward createSCP(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        executeFactoryMethod();
        addActionMessage(request, "message.registration.addNewSCP.success");

        request.setAttribute("registration", ((StudentCurricularPlanCreator) getRenderedObject()).getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

}
