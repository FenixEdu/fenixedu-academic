/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Master Degree Candidate
 *  
 */
public class CreateCandidateDispatchAction extends DispatchAction
{

	public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{

		HttpSession session = request.getSession(false);

		if (session != null)
		{
			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			// Get Execution Year List

			ArrayList executionYearList = null;
			try
			{
				executionYearList = (ArrayList) serviceManager.executar(userView, "ReadExecutionYears", null);
			} catch (ExistingServiceException e)
			{
				throw new ExistingActionException(e);
			}

			request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearList);

			return mapping.findForward("PrepareSuccess");
		} else
			throw new Exception();

	}

	public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{

		HttpSession session = request.getSession(false);

		if (session != null)
		{
			//			DynaActionForm chooseExecutionYearForm = (DynaActionForm) form;
			//			GestorServicos serviceManager = GestorServicos.manager();
			//			
			//			IUserView userView = (IUserView)
			// session.getAttribute(SessionConstants.U_VIEW);

			session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));

			return mapping.findForward("CreateReady");
		} else
			throw new Exception();
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		HttpSession session = request.getSession(false);

		if (session != null)
		{
			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			// Create the Degree Type List
			ArrayList specializations = Specialization.toArrayList();
			request.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);

			// Get the Degree List

			ArrayList degreeList = null;

			DynaActionForm createCandidateForm = (DynaActionForm) form;
			String executionYearName = (String) createCandidateForm.get("executionYear");

			Object args[] = { executionYearName };

			//session.removeAttribute(SessionConstants.EXECUTION_YEAR);

			try
			{
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
			} catch (ExistingServiceException e)
			{
				throw new ExistingActionException(e);
			}

			BeanComparator nameComparator = new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
			Collections.sort(degreeList, nameComparator);

			request.setAttribute(SessionConstants.DEGREE_LIST, degreeList);
			// Create the type of Identification Document
			request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST, TipoDocumentoIdentificacao.toArrayList());

			return mapping.findForward("PrepareSuccess");
		} else
			throw new Exception();

	}

	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DynaActionForm createCandidateForm = (DynaActionForm) form;

		GestorServicos serviceManager = GestorServicos.manager();

		IUserView userView = SessionUtils.getUserView(request);

		// Get the Information
		String degreeType = (String) createCandidateForm.get("specialization");
		Integer executionDegreeOID = new Integer((String) createCandidateForm.get("executionDegreeOID"));
		String name = (String) createCandidateForm.get("name");
		String identificationDocumentNumber = (String) createCandidateForm.get("identificationDocumentNumber");
		String identificationDocumentType = (String) createCandidateForm.get("identificationDocumentType");
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setIdInternal(executionDegreeOID);

		// Create the new Master Degree Candidate
		InfoMasterDegreeCandidate newMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		newMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setNome(name);
		infoPerson.setNumeroDocumentoIdentificacao(identificationDocumentNumber);
		infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(identificationDocumentType));
		newMasterDegreeCandidate.setSpecialization(new Specialization(degreeType));
		newMasterDegreeCandidate.setInfoPerson(infoPerson);

		Object args[] = { newMasterDegreeCandidate };
		InfoMasterDegreeCandidate createdCandidate = null;
		try
		{
			createdCandidate = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "CreateMasterDegreeCandidate", args);
		} catch (ExistingServiceException e)
		{
			throw new ExistingActionException("O Candidato", e);
		}
		request.setAttribute(SessionConstants.NEW_MASTER_DEGREE_CANDIDATE, createdCandidate);
		return mapping.findForward("CreateSuccess");
	}
}
