/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Specialization;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Master Degree Candidate
 *  
 */
public class CreateCandidateDispatchAction extends DispatchAction {

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

        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //Get the Chosen Master Degree
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
            Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));

            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");

            }
            request.setAttribute("curricularPlanID", curricularPlanID);

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
            List specializations = Specialization.toArrayList();
            request.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
            Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));

            String executionDegreeId = (String) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
            if (executionDegreeId == null) {
                executionDegreeId = request.getParameter(SessionConstants.EXECUTION_DEGREE);
            }
            session.setAttribute(SessionConstants.EXECUTION_YEAR, session
                    .getAttribute(SessionConstants.EXECUTION_YEAR));

            if (curricularPlanID == null) {
                curricularPlanID = (Integer) request.getAttribute("curricularPlanID");

            }
            request.setAttribute("curricularPlanID", curricularPlanID);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, executionDegreeId);

            // Get the Degree List

            /*
             * List degreeList = null;
             * 
             * String executionYearName = (String)
             * createCandidateForm.get("executionYear");
             * 
             * Object args[] = { executionYearName };
             * 
             * //session.removeAttribute(SessionConstants.EXECUTION_YEAR);
             * 
             * try { degreeList = (ArrayList)
             * ServiceManagerServiceFactory.executeService(userView,
             * "ReadMasterDegrees", args); } catch (ExistingServiceException e) {
             * throw new ExistingActionException("Curso" ,e); }
             * 
             * //BeanComparator nameComparator = new
             * BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
             * //Collections.sort(degreeList, nameComparator);
             * Collections.sort(degreeList, new
             * ComparatorByNameForInfoExecutionDegree()); List newDegreeList =
             * degreeList; List executionDegreeLabels =
             * buildExecutionDegreeLabelValueBean(newDegreeList);
             * 
             * request.setAttribute(SessionConstants.DEGREE_LIST,
             * executionDegreeLabels);
             */
            // Create the type of Identification Document
            request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST,
                    TipoDocumentoIdentificacao.toArrayList());

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm createCandidateForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        // Get the Information
        String degreeType = (String) createCandidateForm.get("specialization");
        Integer executionDegreeOID = new Integer((String) createCandidateForm.get("executionDegreeOID"));

        String name = (String) createCandidateForm.get("name");
        String identificationDocumentNumber = (String) createCandidateForm
                .get("identificationDocumentNumber");
        String identificationDocumentType = (String) createCandidateForm
                .get("identificationDocumentType");
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setIdInternal(executionDegreeOID);

        // Create the new Master Degree Candidate
        InfoMasterDegreeCandidate newMasterDegreeCandidate = new InfoMasterDegreeCandidate();
        newMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setNome(name);
        infoPerson.setNumeroDocumentoIdentificacao(identificationDocumentNumber);
        infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
                identificationDocumentType));
        newMasterDegreeCandidate.setSpecialization(new Specialization(degreeType));
        newMasterDegreeCandidate.setInfoPerson(infoPerson);

        Object args[] = { newMasterDegreeCandidate };
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