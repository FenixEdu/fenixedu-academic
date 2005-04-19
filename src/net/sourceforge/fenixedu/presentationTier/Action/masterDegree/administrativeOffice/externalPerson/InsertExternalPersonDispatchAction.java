package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.Globals;
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

        request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                .getAttribute(Globals.LOCALE_KEY)));
        return mapping.findForward("start");

    }

    private List getWorkLocations(HttpServletRequest request) throws FenixActionException,
            FenixFilterException {
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

                workLocationsValueBeanList.add(new LabelValueBean("", ""));
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
        String sex = (String) insertExternalPersonForm.get("sex");
        Integer workLocationID = (Integer) insertExternalPersonForm.get("workLocationID");
        String address = (String) insertExternalPersonForm.get("address");
        String phone = (String) insertExternalPersonForm.get("phone");
        String mobile = (String) insertExternalPersonForm.get("mobile");
        String homepage = (String) insertExternalPersonForm.get("homepage");
        String email = (String) insertExternalPersonForm.get("email");

        if (workLocationID == 0) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            getWorkLocations(request);

            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("label.masterDegree.administrativeOffice.workLocationRequired",
                    new ActionError("label.masterDegree.administrativeOffice.workLocationRequired"));

            saveErrors(request, actionErrors);
            return mapping.findForward("start");
        }

        Object args[] = { name, sex, address, workLocationID, phone, mobile, homepage, email };

        try {
            ServiceUtils.executeService(userView, "InsertExternalPerson", args);
        } catch (ExistingServiceException e) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            getWorkLocations(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            request.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request
                    .getAttribute(Globals.LOCALE_KEY)));
            getWorkLocations(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

}