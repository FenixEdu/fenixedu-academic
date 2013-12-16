/*
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.ReadExternalPersonsByInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.ReadAllInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
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
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/visualizeExternalPersons",
        input = "/visualizeExternalPersons.do?page=0&method=prepare", attribute = "visualizeExternalPersonsForm",
        formBean = "visualizeExternalPersonsForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "error", path = "df.page.showExternalPersons_Error", tileProperties = @Tile(title = "teste29")),
        @Forward(name = "start", path = "df.page.visualizeExternalPersons", tileProperties = @Tile(title = "teste30")),
        @Forward(name = "success", path = "df.page.showExternalPersons", tileProperties = @Tile(title = "teste31")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class VisualizeExternalPersonsDispatchAction extends FenixDispatchAction {

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

    public ActionForward visualize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();

        DynaActionForm visualizeExternalPersonsForm = (DynaActionForm) form;
        String institutionId = (String) visualizeExternalPersonsForm.get("institutionId");

        List infoExternalPersons = null;

        ActionErrors actionErrors = new ActionErrors();

        try {

            infoExternalPersons = ReadExternalPersonsByInstitution.run(institutionId);

            if ((infoExternalPersons == null) || (infoExternalPersons.isEmpty())) {
                actionErrors.add("label.masterDegree.administrativeOffice.nonExistingExternalPersons", new ActionError(
                        "label.masterDegree.administrativeOffice.nonExistingExternalPersons"));

                saveErrors(request, actionErrors);
                return mapping.findForward("error");
            }

            request.setAttribute(PresentationConstants.EXTERNAL_PERSONS_LIST, infoExternalPersons);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("success");
    }
}