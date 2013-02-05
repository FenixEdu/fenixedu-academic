/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
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
@Mapping(path = "/manageEnrolmentModel", module = "academicAdministration")
@Forwards({ @Forward(name = "showManageEnrolmentModel", path = "/academicAdminOffice/manageEnrolmentModel.jsp") })
public class ManageEnrolmentModelDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        Registration registration =
                rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request, "registrationID"));
        EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = new EnrolmentModelFactoryEditor(registration);

        request.setAttribute("enrolmentModelBean", enrolmentModelFactoryEditor);
        return mapping.findForward("showManageEnrolmentModel");
    }

    public ActionForward setEnrolmentModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = null;

        if (RenderUtils.getViewState() != null) {
            executeFactoryMethod();
            enrolmentModelFactoryEditor = (EnrolmentModelFactoryEditor) RenderUtils.getViewState().getMetaObject().getObject();
        }

        return redirect("/student.do?method=visualizeRegistration&registrationID="
                + enrolmentModelFactoryEditor.getRegistration().getIdInternal(), request);
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
