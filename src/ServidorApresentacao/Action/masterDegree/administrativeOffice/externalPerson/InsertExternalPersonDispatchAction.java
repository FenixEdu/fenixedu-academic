package ServidorApresentacao.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoWorkLocation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class InsertExternalPersonDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors actionErrors = new ActionErrors();
        List workLocations = getWorkLocations(request);

        if ((workLocations == null) || (workLocations.isEmpty())) {
            actionErrors.add("label.masterDegree.administrativeOffice.nonExistingWorkLocations",
                    new ActionError("label.masterDegree.administrativeOffice.nonExistingWorkLocations"));

            saveErrors(request, actionErrors);
            return mapping.findForward("error");
        }

        return mapping.findForward("start");

    }

    private List getWorkLocations(HttpServletRequest request) throws FenixActionException {
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

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm insertExternalPersonForm = (DynaActionForm) form;

        String name = (String) insertExternalPersonForm.get("name");
        Integer workLocationID = (Integer) insertExternalPersonForm.get("workLocationID");
        String address = (String) insertExternalPersonForm.get("address");
        String phone = (String) insertExternalPersonForm.get("phone");
        String mobile = (String) insertExternalPersonForm.get("mobile");
        String homepage = (String) insertExternalPersonForm.get("homepage");
        String email = (String) insertExternalPersonForm.get("email");

        Object args[] = { name, address, workLocationID, phone, mobile, homepage, email };

        try {
            ServiceUtils.executeService(userView, "InsertExternalPerson", args);
        } catch (ExistingServiceException e) {
            getWorkLocations(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            getWorkLocations(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

}