/*
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

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
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class VisualizeExternalPersonsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();
        Object args[] = {};

        try {
            List infoWorkLocations = (List) ServiceUtils.executeService(userView,
                    "ReadAllWorkLocations", args);

            if (infoWorkLocations != null) {
                if (infoWorkLocations.isEmpty() == false) {
                    List infoWorkLocationsValueBeanList = new ArrayList();
                    Iterator it = infoWorkLocations.iterator();
                    InfoWorkLocation infoWorkLocation = null;

                    while (it.hasNext()) {
                        infoWorkLocation = (InfoWorkLocation) it.next();
                        infoWorkLocationsValueBeanList
                                .add(new LabelValueBean(infoWorkLocation.getName(), infoWorkLocation
                                        .getIdInternal().toString()));
                    }

                    request.setAttribute(SessionConstants.WORK_LOCATIONS_LIST,
                            infoWorkLocationsValueBeanList);
                }
            }

            if ((infoWorkLocations == null) || (infoWorkLocations.isEmpty())) {
                actionErrors.add("label.masterDegree.administrativeOffice.nonExistingWorkLocations",
                        new ActionError(
                                "label.masterDegree.administrativeOffice.nonExistingWorkLocations"));

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
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm visualizeExternalPersonsForm = (DynaActionForm) form;
        Integer workLocationId = (Integer) visualizeExternalPersonsForm.get("workLocationId");

        List infoExternalPersons = null;

        ActionErrors actionErrors = new ActionErrors();
        Object args[] = { workLocationId };

        try {

            infoExternalPersons = (List) ServiceUtils.executeService(userView,
                    "ReadExternalPersonsByWorkLocation", args);

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