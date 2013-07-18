package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveExecutionDegreebyDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentsFromDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to display all the master degrees.
 * 
 */
public class MasterDegreeListingDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DegreeType degreeType = DegreeType.MASTER_DEGREE;

        List result = null;
        try {
            result = ReadAllMasterDegrees.run(degreeType);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O Degree de Mestrado", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, result);

        return mapping.findForward("DisplayMasterDegreeList");
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Get the Chosen Master Degree
        Integer masterDegreeID = new Integer(request.getParameter("degreeID"));
        if (masterDegreeID == null) {
            masterDegreeID = (Integer) request.getAttribute("degreeID");
        }

        List result = null;

        try {

            result = ReadCPlanFromChosenMasterDegree.run(masterDegreeID);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O plano curricular ", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

        return mapping.findForward("MasterDegreeReady");
    }

    public ActionForward prepareList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return getStudentsFromDCP(mapping, form, request, response);
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return The Registration's from a Degree Curricular Plan
     * @throws Exception
     */
    public ActionForward getStudentsFromDCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        // Get the Selected Degree Curricular Plan
        Integer degreeCurricularPlanID = new Integer(request.getParameter("curricularPlanID"));

        List result = null;

        try {
            result =
                    ReadStudentsFromDegreeCurricularPlan.runReadStudentsFromDegreeCurricularPlan(degreeCurricularPlanID,
                            DegreeType.MASTER_DEGREE);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            Integer degreeID = new Integer(request.getParameter("degreeID"));
            request.setAttribute("degreeID", degreeID);

            ActionErrors errors = new ActionErrors();
            errors.add("error.exception.noStudents", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);

            return mapping.findForward("NoStudents");
        }
        BeanComparator numberComparator = new BeanComparator("infoStudent.number");
        Collections.sort(result, numberComparator);

        request.setAttribute(PresentationConstants.STUDENT_LIST, result);

        InfoExecutionDegree infoExecutionDegree = null;
        try {

            infoExecutionDegree = ReadActiveExecutionDegreebyDegreeCurricularPlanID.run(degreeCurricularPlanID);
        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionDegree != null) {
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }

        return mapping.findForward("CurricularPlanReady");
    }
}