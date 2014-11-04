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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.ManageEnrolmentModelBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/manageEnrolmentModel", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showManageEnrolmentModel", path = "/academicAdminOffice/manageEnrolmentModel.jsp") })
public class ManageEnrolmentModelDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Registration registration = getDomainObject(request, "registrationID");
        EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = new EnrolmentModelFactoryEditor(registration);

        request.setAttribute("enrolmentModelBean", enrolmentModelFactoryEditor);
        return mapping.findForward("showManageEnrolmentModel");
    }

    public ActionForward setEnrolmentModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = null;

        if (RenderUtils.getViewState() != null) {
            executeFactoryMethod();
            enrolmentModelFactoryEditor = (EnrolmentModelFactoryEditor) RenderUtils.getViewState().getMetaObject().getObject();
        }

        return redirect("/student.do?method=visualizeRegistration&registrationID="
                + enrolmentModelFactoryEditor.getRegistration().getExternalId(), request);
    }

    public ActionForward postback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EnrolmentModelFactoryEditor enrolmentModelFactoryEditor =
                (EnrolmentModelFactoryEditor) getObjectFromViewState("enrolmentModelBean");
        RenderUtils.invalidateViewState();

        enrolmentModelFactoryEditor.setEnrolmentModel(enrolmentModelFactoryEditor.getRegistration()
                .getEnrolmentModelForExecutionYear(enrolmentModelFactoryEditor.getExecutionYear()));

        request.setAttribute("enrolmentModelBean", enrolmentModelFactoryEditor);
        return mapping.findForward("showManageEnrolmentModel");
    }

    public static class ExecutionYearForRegistrationProvider extends AbstractDomainObjectProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            ((EnrolmentModelFactoryEditor) source).getRegistration().getStartExecutionYear();

            List<ExecutionYear> executionYearList = new ArrayList<ExecutionYear>();
            ExecutionYear iterator = ((EnrolmentModelFactoryEditor) source).getRegistration().getStartExecutionYear();
            while (ExecutionYear.readCurrentExecutionYear().getNextExecutionYear() != iterator) {
                executionYearList.add(iterator);
                iterator = iterator.getNextExecutionYear();
            }

            return executionYearList;
        }
    }

    public static class EnrolmentModelFactoryEditor extends ManageEnrolmentModelBean implements FactoryExecutor {
        public EnrolmentModelFactoryEditor(final Registration registration) {
            super(registration);
        }

        @Override
        public Object execute() {
            getRegistration().setEnrolmentModelForExecutionYear(getExecutionYear(), getEnrolmentModel());
            return null;
        }
    }
}
