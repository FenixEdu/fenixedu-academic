package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class ChooseDataToCreateGuideDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            DegreeType degreeType = DegreeType.MASTER_DEGREE;

            Object args[] = { degreeType };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllMasterDegrees", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("O Degree de Mestrado", e);
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