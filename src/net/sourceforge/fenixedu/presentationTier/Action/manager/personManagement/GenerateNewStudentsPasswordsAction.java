/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsAction extends FenixDispatchAction {

    public ActionForward prepareGeneratePasswords(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("prepare");
    }

    public ActionForward generatePasswords(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        IUserView userView = getUserView(request);
        DynaActionForm passwordsForm = (DynaActionForm) form;
        String fromNumber = (String) passwordsForm.get("fromNumber");
        String toNumber = (String) passwordsForm.get("toNumber");
        List infoPersonList = null;

        try {
            Object args[] = { new Integer(fromNumber), new Integer(toNumber) };
            infoPersonList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "GenerateNewStudentsPasswords", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        Collections.sort(infoPersonList,new BeanComparator("username"));
        
        request.setAttribute("infoPersonList", infoPersonList);

        return mapping.findForward("success");
    }

}