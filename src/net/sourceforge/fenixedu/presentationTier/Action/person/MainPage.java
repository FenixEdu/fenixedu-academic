package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class MainPage extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView };
        try {
            InfoPerson infoPerson = (InfoPerson) ServiceUtils.executeService(userView,
                    "ReadPersonByUsername", args);
            if (infoPerson.getInfoAdvisories() == null || infoPerson.getInfoAdvisories().isEmpty()) {
                infoPerson.setInfoAdvisories(new ArrayList());
            } else {
                Collections.sort(infoPerson.getInfoAdvisories(), new ReverseComparator(
                        new BeanComparator("created")));

                String selectedOidString = request.getParameter("activeAdvisory");
                Integer selectedOid = null;
                if (selectedOidString != null) {
                    selectedOid = new Integer(selectedOidString);
                } else {
                    selectedOid = ((InfoAdvisory) infoPerson.getInfoAdvisories().get(0)).getIdInternal();
                }

                request.setAttribute("activeAdvisory", selectedOid);
            }
            request.setAttribute(SessionConstants.LIST_ADVISORY, infoPerson.getInfoAdvisories());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("ShowWelcomePage");
    }
}