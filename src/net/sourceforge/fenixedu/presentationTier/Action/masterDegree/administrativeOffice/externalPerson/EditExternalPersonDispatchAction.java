package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class EditExternalPersonDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm editExternalPersonForm = (DynaActionForm) form;

        Integer externalPersonId;
        try {
            externalPersonId = new Integer(this.getFromRequest("id", request));
        } catch (NumberFormatException e1) {
            externalPersonId = (Integer) editExternalPersonForm.get("externalPersonID");
        }

        ActionErrors actionErrors = new ActionErrors();
        InfoExternalPerson infoExternalPerson = null;

        Object args[] = { externalPersonId };
        try {
            infoExternalPerson = (InfoExternalPerson) ServiceUtils.executeService(userView,
                    "ReadExternalPersonByID", args);
        } catch (NonExistingServiceException e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        editExternalPersonForm.set("externalPersonID", externalPersonId);
        editExternalPersonForm.set("name", infoExternalPerson.getInfoPerson().getNome());
        editExternalPersonForm.set("workLocationID", infoExternalPerson.getInfoWorkLocation()
                .getIdInternal());
        editExternalPersonForm.set("address", infoExternalPerson.getInfoPerson().getMorada());
        editExternalPersonForm.set("phone", infoExternalPerson.getInfoPerson().getTelefone());
        editExternalPersonForm.set("mobile", infoExternalPerson.getInfoPerson().getTelemovel());
        editExternalPersonForm.set("homepage", infoExternalPerson.getInfoPerson().getEnderecoWeb());
        editExternalPersonForm.set("email", infoExternalPerson.getInfoPerson().getEmail());

        List workLocations = getWorkLocations(request);

        if ((workLocations == null) || (workLocations.isEmpty())) {
            actionErrors.add("label.masterDegree.administrativeOffice.nonExistingWorkLocations",
                    new ActionError("label.masterDegree.administrativeOffice.nonExistingWorkLocations"));

            saveErrors(request, actionErrors);
            return mapping.findForward("error");
        }

        return mapping.findForward("start");

    }

    private List getWorkLocations(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        List workLocations = null;

        Object args[] = {};
        try {
            workLocations = (ArrayList) ServiceUtils.executeService(userView, "ReadAllWorkLocations",
                    args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (workLocations != null)
            if (workLocations.isEmpty() == false) {
                List workLocationsValueBeanList = new ArrayList();
                Iterator it = workLocations.iterator();
                InfoWorkLocation infoWorkLocation = null;

                while (it.hasNext()) {
                    infoWorkLocation = (InfoWorkLocation) it.next();
                    workLocationsValueBeanList.add(new LabelValueBean(infoWorkLocation.getName(),
                            infoWorkLocation.getIdInternal().toString()));
                }

                request.setAttribute(SessionConstants.WORK_LOCATIONS_LIST, workLocationsValueBeanList);
            }
        return workLocations;
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editExternalPersonForm = (DynaActionForm) form;

        Integer externalPersonId = (Integer) editExternalPersonForm.get("externalPersonID");
        String name = (String) editExternalPersonForm.get("name");
        Integer workLocationID = (Integer) editExternalPersonForm.get("workLocationID");
        String address = (String) editExternalPersonForm.get("address");
        String phone = (String) editExternalPersonForm.get("phone");
        String mobile = (String) editExternalPersonForm.get("mobile");
        String homepage = (String) editExternalPersonForm.get("homepage");
        String email = (String) editExternalPersonForm.get("email");

        Object args[] = { externalPersonId, name, address, workLocationID, phone, mobile, homepage,
                email };

        try {
            ServiceUtils.executeService(userView, "EditExternalPerson", args);
        } catch (ExistingServiceException e) {
            getWorkLocations(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            getWorkLocations(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}