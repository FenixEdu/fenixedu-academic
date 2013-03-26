package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.extraCurricularActivities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageExtraCurricularActivities", module = "academicAdministration")
@Forwards({ @Forward(name = "list", path = "/academicAdminOffice/extraCurricularActivities/listActivities.jsp"),
        @Forward(name = "create", path = "/academicAdminOffice/extraCurricularActivities/createActivity.jsp"),
        @Forward(name = "edit", path = "/academicAdminOffice/extraCurricularActivities/editActivity.jsp") })
public class ManageExtraCurricularActivities extends FenixDispatchAction {
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("activityTypes", rootDomainObject.getExtraCurricularActivityTypeSet());
        return mapping.findForward("list");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("create");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivityType type = getDomainObject(request, "activityTypeId");
        if (type.hasAnyExtraCurricularActivity()) {
            addErrorMessage(request, "errors", "error.extraCurricularActivityTypes.unableToEditUsedType", type.getName()
                    .getContent());
            return list(mapping, actionForm, request, response);
        }
        request.setAttribute("extraCurricularActivityType", type);
        return mapping.findForward("edit");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivityType type = getDomainObject(request, "activityTypeId");
        try {
            type.delete();
        } catch (DomainException e) {
            addErrorMessage(request, "errors", e.getKey(), e.getArgs());
        }
        return list(mapping, actionForm, request, response);
    }
}
