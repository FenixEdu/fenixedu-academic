package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.Globals;
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

public class InsertExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors actionErrors = new ActionErrors();
        List institutions = getInstitutions(request);

        if ((institutions == null) || (institutions.isEmpty())) {
            actionErrors.add("label.masterDegree.administrativeOffice.nonExistingInstitutions",
                    new ActionError("label.masterDegree.administrativeOffice.nonExistingInstitutions"));

            saveErrors(request, actionErrors);
            return mapping.findForward("error");
        }

        request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                .getAttribute(Globals.LOCALE_KEY)));
        return mapping.findForward("start");

    }

    private List getInstitutions(HttpServletRequest request) throws FenixActionException,
            FenixFilterException {
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
                Collections.sort(institutions, new BeanComparator("name"));
                List institutionsValueBeanList = new ArrayList();
                Iterator it = institutions.iterator();
                Unit infoInstitutions = null;

                institutionsValueBeanList.add(new LabelValueBean("", ""));
                while (it.hasNext()) {
                    infoInstitutions = (Unit) it.next();
                    institutionsValueBeanList.add(new LabelValueBean(infoInstitutions.getName(),
                            infoInstitutions.getIdInternal().toString()));
                }

                request.setAttribute(SessionConstants.WORK_LOCATIONS_LIST, institutionsValueBeanList);
            }
        return institutions;
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm insertExternalPersonForm = (DynaActionForm) form;

        String name = (String) insertExternalPersonForm.get("name");
        String sex = (String) insertExternalPersonForm.get("sex");
        Integer institutionID = (Integer) insertExternalPersonForm.get("institutionID");
        String address = (String) insertExternalPersonForm.get("address");
        String phone = (String) insertExternalPersonForm.get("phone");
        String mobile = (String) insertExternalPersonForm.get("mobile");
        String homepage = (String) insertExternalPersonForm.get("homepage");
        String email = (String) insertExternalPersonForm.get("email");

        if (institutionID == 0) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            
            getInstitutions(request);

            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("label.masterDegree.administrativeOffice.institutionRequired",
                    new ActionError("label.masterDegree.administrativeOffice.institutionRequired"));

            saveErrors(request, actionErrors);
            return mapping.findForward("start");
        }

        Object args[] = { name, sex, address, institutionID, phone, mobile, homepage, email };

        try {
            ServiceUtils.executeService(userView, "InsertExternalPerson", args);
        } catch (ExistingServiceException e) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            getInstitutions(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            getInstitutions(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

}