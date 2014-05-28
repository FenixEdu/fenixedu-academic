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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear.EnrolmentModelFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
}
