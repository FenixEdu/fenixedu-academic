/*
 * Created on 28/Jul/2004
 *
 */
package ServidorApresentacao.Action.manager.precedences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixDispatchAction;


/**
 * @author Tânia Pousão
 *
 */
public class MakeSimplePrecedenceAction extends FenixDispatchAction {

    public ActionForward showAllRestrictions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("showAllRestrictions");
    }
    
    public ActionForward insertRestriction(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String className = request.getParameter("className");
        System.out.println("Class Name Restriction: " + className);
                
        
        return mapping.findForward("");
    }
}
