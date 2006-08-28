/*
 * VisualizeApplicationInfoAction.java
 * 
 * 
 * Created on 07 de Dezembro de 2002, 11:16
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class VisualizePersonalInfoAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        InfoPerson infoPerson = null;

        Object args[] = { userView.getUtilizador() };

        infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                "ReadPersonByUsername", args);

        request.removeAttribute("personalInfo");

        request.setAttribute("personalInfo", infoPerson);

        return mapping.findForward("Success");
    }

}