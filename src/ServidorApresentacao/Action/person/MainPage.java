package ServidorApresentacao.Action.person;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoAdvisory;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

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