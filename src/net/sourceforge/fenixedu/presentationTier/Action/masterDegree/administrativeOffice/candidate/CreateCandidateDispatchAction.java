/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate;

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
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *         This is the Action to create a Master Degree Candidate
 */
public class CreateCandidateDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);

            IUserView userView = getUserView(request);

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

            IUserView userView = getUserView(request);

            // Get the Chosen Master Degree
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

        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = getUserView(request);

            // Get the Chosen Master Degree
            Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
            }
            request.setAttribute("curricularPlanID", curricularPlanID);

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

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
            Integer curricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));

            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");

            }
            request.setAttribute("degreeCurricularPlanID", curricularPlanID);

            request.setAttribute(SessionConstants.EXECUTION_DEGREE, request
                    .getParameter("executionDegreeID"));
            return mapping.findForward("CreateReady");
        }
        throw new Exception();
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            // Create the Degree Type List

            String executionDegreeId = (String) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
            if (executionDegreeId == null) {
                executionDegreeId = request.getParameter(SessionConstants.EXECUTION_DEGREE);
            }
            session.setAttribute(SessionConstants.EXECUTION_YEAR, session
                    .getAttribute(SessionConstants.EXECUTION_YEAR));

            Integer curricularPlanID = null;
            String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            if (degreeCurricularPlanID != null) {
                curricularPlanID = Integer.valueOf(degreeCurricularPlanID);
            } else {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
            }

            request.setAttribute("curricularPlanID", curricularPlanID);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, executionDegreeId);

            // Get the Degree List

            /*
             * List degreeList = null; String executionYearName = (String)
             * createCandidateForm.get("executionYear"); Object args[] = {
             * executionYearName };
             * //session.removeAttribute(SessionConstants.EXECUTION_YEAR); try {
             * degreeList = (ArrayList)
             * ServiceManagerServiceFactory.executeService(userView,
             * "ReadMasterDegrees", args); } catch (ExistingServiceException e) {
             * throw new ExistingActionException("Degree" ,e); }
             * //BeanComparator nameComparator = new
             * BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
             * //Collections.sort(degreeList, nameComparator);
             * Collections.sort(degreeList, new
             * ComparatorByNameForInfoExecutionDegree()); List newDegreeList =
             * degreeList; List executionDegreeLabels =
             * buildExecutionDegreeLabelValueBean(newDegreeList);
             * request.setAttribute(SessionConstants.DEGREE_LIST,
             * executionDegreeLabels);
             */
            // Create the type of Identification Document
            /*
             * request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST,
             * TipoDocumentoIdentificacao.toArrayList());
             */

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        // Get the Information
        DynaActionForm createCandidateForm = (DynaActionForm) form;
        String degreeType = (String) createCandidateForm.get("specialization");
        Integer executionDegreeOID = new Integer((String) createCandidateForm.get("executionDegreeOID"));
        String name = (String) createCandidateForm.get("name");
        String identificationDocumentNumber = (String) createCandidateForm
                .get("identificationDocumentNumber");
        String identificationDocumentType = (String) createCandidateForm
                .get("identificationDocumentType");

        Object args[] = { Specialization.valueOf(degreeType), executionDegreeOID, name,
                identificationDocumentNumber, IDDocumentType.valueOf(identificationDocumentType) };

        InfoMasterDegreeCandidate createdCandidate = null;
        try {
            createdCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory.executeService(
                    userView, "CreateMasterDegreeCandidate", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("O Candidato", e);
        }

        request.setAttribute(SessionConstants.NEW_MASTER_DEGREE_CANDIDATE, createdCandidate);
        return mapping.findForward("CreateSuccess");
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

}