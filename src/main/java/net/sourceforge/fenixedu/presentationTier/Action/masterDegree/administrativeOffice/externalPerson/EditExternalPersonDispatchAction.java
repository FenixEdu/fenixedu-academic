package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.EditExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.ReadExternalPersonByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.ReadAllInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/editExternalPerson",
        input = "/editExternalPerson.do?page=0&method=prepare", attribute = "editExternalPersonForm",
        formBean = "editExternalPersonForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "error", path = "df.page.editExternalPerson_Error"),
        @Forward(name = "start", path = "df.page.editExternalPerson"),
        @Forward(name = "success", path = "df.page.editExternalPerson_Success") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm editExternalPersonForm = (DynaActionForm) form;

        String externalPersonId;
        try {
            externalPersonId = this.getFromRequest("id", request);
        } catch (Exception e1) {
            externalPersonId = (String) editExternalPersonForm.get("externalPersonID");
        }

        InfoExternalPerson infoExternalPerson = null;

        try {
            infoExternalPerson = (InfoExternalPerson) ReadExternalPersonByID.run(externalPersonId);
        } catch (NonExistingServiceException e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        editExternalPersonForm.set("externalPersonID", externalPersonId);
        editExternalPersonForm.set("name", infoExternalPerson.getInfoPerson().getNome());
        editExternalPersonForm.set("institutionID", infoExternalPerson.getInfoInstitution().getExternalId());
        editExternalPersonForm.set("address", infoExternalPerson.getInfoPerson().getMorada());
        editExternalPersonForm.set("phone", infoExternalPerson.getInfoPerson().getTelefone());
        editExternalPersonForm.set("mobile", infoExternalPerson.getInfoPerson().getTelemovel());
        editExternalPersonForm.set("homepage", infoExternalPerson.getInfoPerson().getEnderecoWeb());
        editExternalPersonForm.set("email", infoExternalPerson.getInfoPerson().getEmail());

        List institutions = getInstitutions(request);

        if ((institutions == null) || (institutions.isEmpty())) {
            addErrorMessage(request, "label.masterDegree.administrativeOffice.nonExistingInstitutions",
                    "label.masterDegree.administrativeOffice.nonExistingInstitutions");
            return mapping.findForward("error");
        }

        return mapping.findForward("start");

    }

    private List getInstitutions(HttpServletRequest request) throws FenixActionException {
        List institutions = null;

        try {
            institutions = (ArrayList) ReadAllInstitutions.run();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (institutions != null) {
            if (institutions.isEmpty() == false) {
                List institutionsValueBeanList = new ArrayList();
                Iterator it = institutions.iterator();
                Unit infoInstitution = null;

                while (it.hasNext()) {
                    infoInstitution = (Unit) it.next();
                    institutionsValueBeanList.add(new LabelValueBean(infoInstitution.getName(), infoInstitution.getExternalId()
                            .toString()));
                }

                request.setAttribute(PresentationConstants.WORK_LOCATIONS_LIST, institutionsValueBeanList);
            }
        }
        return institutions;
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm editExternalPersonForm = (DynaActionForm) form;

        String externalPersonId = (String) editExternalPersonForm.get("externalPersonID");
        String name = (String) editExternalPersonForm.get("name");
        String institutionID = (String) editExternalPersonForm.get("institutionID");
        String address = (String) editExternalPersonForm.get("address");
        String phone = (String) editExternalPersonForm.get("phone");
        String mobile = (String) editExternalPersonForm.get("mobile");
        String homepage = (String) editExternalPersonForm.get("homepage");
        String email = (String) editExternalPersonForm.get("email");

        try {
            EditExternalPerson.run(externalPersonId, name, address, institutionID, phone, mobile, homepage, email);
        } catch (ExistingServiceException e) {
            getInstitutions(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            getInstitutions(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        } catch (DomainException e) {
            addErrorMessage(request, "error", e.getMessage());
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