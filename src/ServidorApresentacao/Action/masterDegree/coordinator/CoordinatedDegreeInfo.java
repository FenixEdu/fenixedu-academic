/*
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 * modified by Fernanda Quitério
 *  
 */

package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

public class CoordinatedDegreeInfo extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = SessionUtils.getUserView(request);
            List degreeList = (List) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_LIST);

            Integer choosenDegreePosition = findChosenDegreePosition(request);

            // Put the selected Degree in Session
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) degreeList
                    .get(choosenDegreePosition.intValue());
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
            session.setAttribute(SessionConstants.MASTER_DEGREE,
                    infoExecutionDegree);

            List result = null;
            Object argsTemp[] = { infoExecutionDegree };
            try {
                result = (List) ServiceManagerServiceFactory.executeService(
                        userView, "ReadDegreeCandidates", argsTemp);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            session.setAttribute(
                    SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT,
                    new Integer(result.size()));
            return mapping.findForward("Success");
        } else
            throw new Exception();
    }

    /**
     * @param request
     * @return
     */
    private Integer findChosenDegreePosition(HttpServletRequest request) {
        Integer choosenDegreePosition = null;
        if (request.getParameter("degree") != null) {
            choosenDegreePosition = Integer.valueOf(request
                    .getParameter("degree"));
        } else {
            choosenDegreePosition = Integer.valueOf((String) request
                    .getAttribute("degree"));
        }
        return choosenDegreePosition;
    }
}