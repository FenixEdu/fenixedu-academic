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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
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
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class EditExternalPersonDispatchAction extends FenixDispatchAction {

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
        editExternalPersonForm.set("institutionID", infoExternalPerson.getInfoInstitution()
                .getIdInternal());
        editExternalPersonForm.set("address", infoExternalPerson.getInfoPerson().getMorada());
        editExternalPersonForm.set("phone", infoExternalPerson.getInfoPerson().getTelefone());
        editExternalPersonForm.set("mobile", infoExternalPerson.getInfoPerson().getTelemovel());
        editExternalPersonForm.set("homepage", infoExternalPerson.getInfoPerson().getEnderecoWeb());
        editExternalPersonForm.set("email", infoExternalPerson.getInfoPerson().getEmail());

        List institutions = getInstitutions(request);

        if ((institutions == null) || (institutions.isEmpty())) {
            actionErrors.add("label.masterDegree.administrativeOffice.nonExistingInstitutions",
                    new ActionError("label.masterDegree.administrativeOffice.nonExistingInstitutions"));

            saveErrors(request, actionErrors);
            return mapping.findForward("error");
        }

        return mapping.findForward("start");

    }

    private List getInstitutions(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        List institutions = null;

        Object args[] = {};
        try {
            institutions = (ArrayList) ServiceUtils.executeService(userView, "ReadAllInstitutions",
                    args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (institutions != null)
            if (institutions.isEmpty() == false) {
                List institutionsValueBeanList = new ArrayList();
                Iterator it = institutions.iterator();
                Unit infoInstitution = null;

                while (it.hasNext()) {
                    infoInstitution = (Unit) it.next();
                    institutionsValueBeanList.add(new LabelValueBean(infoInstitution.getName(),
                            infoInstitution.getIdInternal().toString()));
                }

                request.setAttribute(SessionConstants.WORK_LOCATIONS_LIST, institutionsValueBeanList);
            }
        return institutions;
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editExternalPersonForm = (DynaActionForm) form;
               
        Integer externalPersonId = (Integer) editExternalPersonForm.get("externalPersonID");
        String name = (String) editExternalPersonForm.get("name");
        Integer institutionID = (Integer) editExternalPersonForm.get("institutionID");
        String address = (String) editExternalPersonForm.get("address");
        String phone = (String) editExternalPersonForm.get("phone");
        String mobile = (String) editExternalPersonForm.get("mobile");
        String homepage = (String) editExternalPersonForm.get("homepage");
        String email = (String) editExternalPersonForm.get("email");

        Object args[] = { externalPersonId, name, address, institutionID, phone, mobile, homepage,
                email };

        try {
            ServiceUtils.executeService(userView, "EditExternalPerson", args);
        } catch (ExistingServiceException e) {
            getInstitutions(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            getInstitutions(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        } catch (DomainException e){
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);              
            return mapping.findForward("error");
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