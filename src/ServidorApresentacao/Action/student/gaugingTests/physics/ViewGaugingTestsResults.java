/*
 * Created on 26/Nov/2003
 *  
 */
package ServidorApresentacao.Action.student.gaugingTests.physics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gaugingTests.physics.InfoGaugingTestResult;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 26/Nov/2003
 *  
 */
public class ViewGaugingTestsResults extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView };
        try {
            InfoGaugingTestResult result = (InfoGaugingTestResult) ServiceUtils.executeService(userView,
                    "readGaugingTestsResults", args);
            request.setAttribute("gaugingTestResult", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("viewGaugingTestsResults");
    }

}