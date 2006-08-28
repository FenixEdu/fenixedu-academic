package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson.institution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */

public class EditInstitutionDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();
        Object args[] = {};

        try {
            List infoInstitutions = (List) ServiceUtils.executeService(userView,
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

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editInstitutionForm = (DynaActionForm) form;

        Integer oldInstitutionId = (Integer) editInstitutionForm.get("institutionId");
        String newInstitutionName = (String) editInstitutionForm.get("name");

        Object args[] = { oldInstitutionId, newInstitutionName };

        try {
            ServiceUtils.executeService(userView, "EditInstitution", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping
                    .findForward("errorLocationAlreadyExists"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("success");
    }

}