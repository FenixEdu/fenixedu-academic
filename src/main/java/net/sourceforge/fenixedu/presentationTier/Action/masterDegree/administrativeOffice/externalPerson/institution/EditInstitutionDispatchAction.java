package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson.institution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.EditInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.ReadAllInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/editInstitution",
        input = "/editInstitution.do?page=0&method=prepare", attribute = "editInstitutionForm", formBean = "editInstitutionForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "error", path = "df.page.editInstitution"),
        @Forward(name = "start", path = "df.page.editInstitution"),
        @Forward(name = "errorLocationAlreadyExists", path = "/editInstitution.do?page=0&method=prepare"),
        @Forward(name = "success", path = "df.page.editInstitution_success") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditInstitutionDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        ActionErrors actionErrors = new ActionErrors();

        try {
            List infoInstitutions = (List) ReadAllInstitutions.run();

            if (infoInstitutions != null) {
                if (infoInstitutions.isEmpty() == false) {
                    Collections.sort(infoInstitutions, new BeanComparator("name"));
                    List infoInstitutionsValueBeanList = new ArrayList();
                    Iterator it = infoInstitutions.iterator();
                    Unit infoInstitution = null;

                    while (it.hasNext()) {
                        infoInstitution = (Unit) it.next();
                        infoInstitutionsValueBeanList.add(new LabelValueBean(infoInstitution.getName(), infoInstitution
                                .getExternalId().toString()));
                    }

                    request.setAttribute(PresentationConstants.WORK_LOCATIONS_LIST, infoInstitutionsValueBeanList);
                }
            }

            if ((infoInstitutions == null) || (infoInstitutions.isEmpty())) {
                actionErrors.add("label.masterDegree.administrativeOffice.nonExistingInstitutions", new ActionError(
                        "label.masterDegree.administrativeOffice.nonExistingInstitutions"));

                saveErrors(request, actionErrors);
                return mapping.findForward("error");
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("start");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();

        DynaActionForm editInstitutionForm = (DynaActionForm) form;

        String oldInstitutionId = (String) editInstitutionForm.get("institutionId");
        String newInstitutionName = (String) editInstitutionForm.get("name");

        try {
            EditInstitution.run(oldInstitutionId, newInstitutionName);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("errorLocationAlreadyExists"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("success");
    }

}