package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends FenixDispatchAction {

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

            request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);

            return mapping.findForward("DisplayMasterDegreeList");
        }
        throw new Exception();
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

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

            request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

            return mapping.findForward("MasterDegreeReady");
        }
        throw new Exception();
    }

    public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        //	HttpSession session = request.getSession(false);

        // Get Execution Year List
        /*
         * MessageResources messages = getResources(request); IUserView userView =
         * (IUserView) session.getAttribute(SessionConstants.U_VIEW); ArrayList
         * executionYearList = null; try { executionYearList = (ArrayList)
         * ServiceManagerServiceFactory.executeService( userView,
         * "ReadNotClosedExecutionYears", null); } catch
         * (ExistingServiceException e) { throw new ExistingActionException(e); }
         * 
         * if (request.getParameter("jspTitle") != null) {
         * request.setAttribute("jspTitle",
         * messages.getMessage(request.getParameter("jspTitle"))); }
         * 
         * List executionYearsLabels = transformIntoLabels(executionYearList);
         * 
         * request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST,
         * executionYearsLabels);
         */
        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //Get the Chosen Master Degree
            Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
            }
            List executionYearList = null;
            Object args[] = { curricularPlanID };
            try {
                executionYearList = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                        "ReadExecutionDegreesByDegreeCurricularPlanID", args);
            } catch (ExistingServiceException e) {
                throw new ExistingActionException(e);
            }
            List executionYearsLabels = transformIntoLabels(executionYearList);
            request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearsLabels);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, curricularPlanID);

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();

    }

    private List transformIntoLabels(List executionYearList) {
        List executionYearsLabels = new ArrayList();
        CollectionUtils.collect(executionYearList, new Transformer() {
            public Object transform(Object input) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                LabelValueBean labelValueBean = new LabelValueBean(infoExecutionDegree
                        .getInfoExecutionYear().getYear(), infoExecutionDegree.getIdInternal()
                        .toString());
                return labelValueBean;
            }
        }, executionYearsLabels);
        Collections.sort(executionYearsLabels, new BeanComparator("label"));
        Collections.reverse(executionYearsLabels);

        return executionYearsLabels;
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
            Integer curricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));

            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanID");

            }

            request.setAttribute(SessionConstants.EXECUTION_DEGREE, request
                    .getParameter("executionDegreeID"));
            request.setAttribute("degreeCurricularPlanID", curricularPlanID);
            return mapping.findForward("ChooseSuccess");
        }
        throw new Exception();

    }

}