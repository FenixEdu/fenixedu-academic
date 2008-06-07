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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class VisualizeExternalPersonsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = UserView.getUser();
        ActionErrors actionErrors = new ActionErrors();
        Object args[] = {};

        try {
            List infoInstitutions = (List) ServiceUtils.executeService(
                    "ReadAllInstitutions", args);

            if (infoInstitutions != null) {
                if (infoInstitutions.isEmpty() == false) {
                    Collections.sort(infoInstitutions, new BeanComparator("name"));
                    List infoInstitutionsValueBeanList = new ArrayList();
                    Iterator it = infoInstitutions.iterator();
                    Unit infoInstitution = null;

                    while (it.hasNext()) {
                        infoInstitution = (Unit) it.next();
                        infoInstitutionsValueBeanList
                                .add(new LabelValueBean(infoInstitution.getName(), infoInstitution
                                        .getIdInternal().toString()));
                    }

                    request.setAttribute(SessionConstants.WORK_LOCATIONS_LIST,
                            infoInstitutionsValueBeanList);
                }
            }

            if ((infoInstitutions == null) || (infoInstitutions.isEmpty())) {
                actionErrors.add("label.masterDegree.administrativeOffice.nonExistingInstitutions",
                        new ActionError(
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
        IUserView userView = UserView.getUser();

        DynaActionForm visualizeExternalPersonsForm = (DynaActionForm) form;
        Integer institutionId = (Integer) visualizeExternalPersonsForm.get("institutionId");

        List infoExternalPersons = null;

        ActionErrors actionErrors = new ActionErrors();
        Object args[] = { institutionId };

        try {

            infoExternalPersons = (List) ServiceUtils.executeService(
                    "ReadExternalPersonsByInstitution", args);

            if ((infoExternalPersons == null) || (infoExternalPersons.isEmpty())) {
                actionErrors.add("label.masterDegree.administrativeOffice.nonExistingExternalPersons",
                        new ActionError(
                                "label.masterDegree.administrativeOffice.nonExistingExternalPersons"));

                saveErrors(request, actionErrors);
                return mapping.findForward("error");
            }

            request.setAttribute(SessionConstants.EXTERNAL_PERSONS_LIST, infoExternalPersons);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("success");
    }
}