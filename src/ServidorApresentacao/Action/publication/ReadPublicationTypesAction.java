/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorApresentacao.Action.publication;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadPublicationTypesAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        if ((session != null)) {
            Object[] args = { userView.getUtilizador() };
            Integer keyTeacher = new Integer(request.getParameter("infoTeacher#idInternal"));
            String typePublication = request.getParameter("typePublication");
            List infoPublicationTypes = (List) ServiceUtils.executeService(userView,
                    "ReadPublicationTypes", args);            
            
            BeanComparator comparator = new BeanComparator("publicationType",Collator.getInstance());
            Collections.sort(infoPublicationTypes,comparator);
            Collections.reverse(infoPublicationTypes);
            
            request.setAttribute("publicationTypesList", infoPublicationTypes);
            dynaForm.set("teacherId", keyTeacher);
            dynaForm.set("typePublication", typePublication);
        }
        ActionForward actionForward = null;

        actionForward = mapping.findForward("show-publication-types-form");

        return actionForward;
    }
}