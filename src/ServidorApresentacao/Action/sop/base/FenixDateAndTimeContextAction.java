/*
 * Created on 2003/07/29
 *
 */
package ServidorApresentacao.Action.sop.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.sop.utils.RequestContextUtil;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz
 */
public abstract class FenixDateAndTimeContextAction extends FenixContextAction {

    /**
     * Tests if the session is valid
     * 
     * @see SessionUtils#validSessionVerification(HttpServletRequest,
     *      ActionMapping)
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
     *      HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        RequestContextUtil.setExamDateAndTimeContext(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}