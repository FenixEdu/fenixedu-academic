/*
 * Created on 11/Dez/2004
 */
package ServidorApresentacao.Action.managementAssiduousness;

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
public class ExtraWorkByEmployeeAction extends FenixDispatchAction {

    public ActionForward prepareInputs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("inputDateAndEmployee");
    }
}
