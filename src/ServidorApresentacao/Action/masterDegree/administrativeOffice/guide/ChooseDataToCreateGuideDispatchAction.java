package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class ChooseDataToCreateGuideDispatchAction extends DispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            TipoCurso degreeType = TipoCurso.MESTRADO_OBJ;

            Object args[] = { degreeType };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllMasterDegrees", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("O Curso de Mestrado", e);
            }

            if ((result != null) && (result.size() > 0)) {
                request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);
            }

            return mapping.findForward("DisplayMasterDegreeList");
        }

        throw new Exception();
    }

    public ActionForward chooseMasterDegreeCurricularPlanFromList(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //Get the Chosen Master Degree
            Integer masterDegreeID = new Integer(request.getParameter("degreeID"));
            if (masterDegreeID == null) {
                masterDegreeID = (Integer) request.getAttribute("degreeID");
            }

            Object args[] = { masterDegreeID };
            List result = null;

            try {

                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadCPlanFromChosenMasterDegree", args);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("O plano curricular ", e);
            }

            if ((result != null) && (result.size() > 0)) {
                request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);
            }

            return mapping.findForward("DisplayMasterDegreeCurricularPlanList");
        }

        throw new Exception();
    }

    public ActionForward chooseExecutionDegreeFromList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //Get execution degrees for given degree curricular plan
            Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
            }

            Object args[] = { curricularPlanID };
            List result = null;

            result = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionDegreesByDegreeCurricularPlanID", args);

            if ((result != null) && (result.size() > 0)) {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE_LIST, result);
            }

            return mapping.findForward("DisplayExecutionDegreeList");
        }

        throw new Exception();

    }

}