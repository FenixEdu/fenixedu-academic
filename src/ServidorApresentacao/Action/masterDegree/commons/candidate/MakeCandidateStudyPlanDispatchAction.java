
package ServidorApresentacao.Action.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCandidateEnrolment;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.SituationName;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class MakeCandidateStudyPlanDispatchAction extends DispatchAction {

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward prepareSelectCandidates(ActionMapping mapping, ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response)
		throws Exception {
			
		HttpSession session = request.getSession(false);

		DynaActionForm approvalForm = (DynaActionForm) form;

		GestorServicos serviceManager = GestorServicos.manager();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionYear = (String) request.getAttribute("executionYear");
		String degree = (String) request.getAttribute("degree");

		if (executionYear == null){
			executionYear = (String) approvalForm.get("executionYear");
		}

		if (degree == null){
			degree = (String) approvalForm.get("degree");
		}
		
		List candidateList = null;
		
		List admitedSituations = new ArrayList();
		admitedSituations.add(new SituationName(SituationName.ADMITIDO));
		admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
		admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_FINALIST));
		admitedSituations.add(new SituationName(SituationName.ADMITED_CONDICIONAL_OTHER));
		
		
		Object args[] = { executionYear, degree , admitedSituations};

		try {
			candidateList = (ArrayList) serviceManager.executar(userView, "ReadCandidatesForSelection", args);
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("error.masterDegree.administrativeOffice.nonExistingAdmitedCandidates"));
			saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}
		
		BeanComparator nameComparator = new BeanComparator("infoPerson.nome");
		Collections.sort(candidateList, nameComparator);
		
//		generateToken(request);
//		saveToken(request);
		
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);

		request.setAttribute("candidateList", candidateList);
		return mapping.findForward("PrepareSuccess");
	}
  
  	/**
  	 * 
  	 * @param mapping
  	 * @param form
  	 * @param request
  	 * @param response
  	 * @return
  	 * @throws Exception
  	 */
	public ActionForward prepareSecondChooseMasterDegree(ActionMapping mapping, ActionForm form,
												HttpServletRequest request,
												HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();

		DynaActionForm chooseSecondMasterDegreeForm = (DynaActionForm) form;
		
		String executionYear = getFromRequest("executionYear", request);
		
		String candidateID = getFromRequest("candidateID", request);
//		request.setAttribute("personID", candidateID);
		chooseSecondMasterDegreeForm.set("candidateID", Integer.valueOf(candidateID));

		request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
		request.setAttribute("executionYear", executionYear);

		// Get the Master Degree List			
		Object args[] = { executionYear };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		ArrayList masterDegreeList = null;
		try {
			
			masterDegreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("message.masterDegree.notfound.degrees", executionYear));
			saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}
		

		Collections.sort(masterDegreeList, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

		request.setAttribute(SessionConstants.DEGREE_LIST, masterDegreeList);

		return mapping.findForward("PrepareSecondChooseMasterDegreeReady");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			DynaActionForm chooseMDForm = (DynaActionForm) form;

			request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
			request.setAttribute("executionYear", getFromRequest("executionYear", request));
			request.setAttribute("candidateID", chooseMDForm.get("candidateID"));
			
			String degree = (String) chooseMDForm.get("masterDegree");
			
			request.setAttribute("degree", degree);
						
			return mapping.findForward("ChooseSuccess");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward prepareSelectCourseList(ActionMapping mapping, ActionForm form, 
							HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		
		DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;

		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);
		String candidateID = getFromRequest("candidateID", request);
		
		request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);


		// Get the Curricular Course List			
		Object args[] = { executionYear, degree };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		ArrayList curricularCourseList = null;
		try {
			curricularCourseList = (ArrayList) serviceManager.executar(userView, "ReadCurricularCoursesByDegree", args);
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("message.public.notfound.curricularCourses"));
			saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}
		
		
		
		orderCourseList(curricularCourseList);
		
		request.setAttribute("curricularCourses", curricularCourseList);


		// Prepare the Form
		chooseCurricularCoursesForm.set("candidateID", Integer.valueOf(candidateID));
		chooseCurricularCoursesForm.set("attributedCredits", new Double(0));
		

//ler cadeiras do aluno e fazer outra lista para mostrar


		return mapping.findForward("PrepareSuccess");
	}
	
	/**
	 * @param curricularCourseList
	 */
	private void orderCourseList(ArrayList curricularCourseList) {
		BeanComparator nameCourse = new BeanComparator("name");
		Collections.sort(curricularCourseList, nameCourse);

		Iterator iterator = curricularCourseList.iterator();
		while(iterator.hasNext()){
			InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
			List scopes = infoCurricularCourse.getInfoScopes();
			
			BeanComparator branchName = new BeanComparator("infoBranch.name");
			Collections.sort(scopes, branchName);
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	UNDER CONSTRUCTION		
	public ActionForward chooseCurricularCourses(ActionMapping mapping, ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response)
			throws Exception {

		
		HttpSession session = request.getSession(false);
		DynaActionForm chooseCurricularCoursesForm = (DynaActionForm) form;
		
		GestorServicos serviceManager = GestorServicos.manager();	
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String[] selection =  (String[]) chooseCurricularCoursesForm.get("selection");

// testar se a selecção nao foi feita e lançar excepçao

	   Integer candidateID = (Integer) chooseCurricularCoursesForm.get("candidateID");
	   
		Double attributedCredits = (Double) chooseCurricularCoursesForm.get("attributedCredits");

		
		try {
			Object args[] = { selection, candidateID , attributedCredits};
			serviceManager.executar(userView, "WriteCandidateEnrolments", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		}

		List candidateEnrolments = null;

		try {
			Object args[] = {  candidateID };
			candidateEnrolments = (List) serviceManager.executar(userView, "ReadCandidateEnrolmentsByCandidateID", args);
		} catch (NonExistingServiceException e) {
			
		}
		
		Iterator coursesIter = candidateEnrolments.iterator();
		float credits = attributedCredits.floatValue();
	
		while(coursesIter.hasNext()){
			InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) coursesIter.next();
			credits += infoCandidateEnrolment.getInfoCurricularCourseScope().getInfoCurricularCourse().getCredits().floatValue();
		}
		
		request.setAttribute("givenCredits", new Double(credits));

		if ((candidateEnrolments != null) && (candidateEnrolments.size() != 0)){
			request.setAttribute("candidateEnrolments", candidateEnrolments);
		}

		InfoExecutionDegree infoExecutionDegree = null;
		try {
			Object args[] = {  candidateID };
			infoExecutionDegree = (InfoExecutionDegree) serviceManager.executar(userView, "ReadExecutionDegreeByCandidateID", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("executionDegree", infoExecutionDegree);
	

		
		return mapping.findForward("ChooseSuccess");
	}
	
	
	
	
	
	
	
	
	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	 
//	public ActionForward print(ActionMapping mapping, ActionForm form,
//									HttpServletRequest request,
//									HttpServletResponse response)
//		throws Exception {
//	
//		HttpSession session = request.getSession(false);
//	
//		DynaActionForm resultForm = (DynaActionForm) form;
//				
//		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
//		GestorServicos serviceManager = GestorServicos.manager();
//	
//		String[] candidateList = (String[]) resultForm.get("situations");
//		String[] ids = (String[]) resultForm.get("candidatesID");
//		String[] remarks = (String[]) resultForm.get("remarks");
//		String[] substitutes = (String[]) resultForm.get("substitutes");
//	
//		
//
//		
//		request.setAttribute("situations", candidateList);
//		request.setAttribute("candidatesID", ids);
//		request.setAttribute("remarks", remarks);
//		request.setAttribute("substitutes", substitutes);
//
//		List candidates = new ArrayList();
//	
//			
//		try {
//			Object args[] = { ids };
//			candidates = (List) serviceManager.executar(userView, "ReadCandidates", args);					
//		} catch (ExistingServiceException e) {
//			throw new ExistingActionException(e);
//		}			
//
//		List result = getLists(candidateList, ids, remarks, substitutes, candidates);
//		
//		sortLists(result);
//		
//		
////		Iterator iterator = result.iterator();
////		while(iterator.hasNext()){
////			InfoCandidateApprovalGroup infoCandidateApprovalGroup = (InfoCandidateApprovalGroup) iterator.next();
////			Iterator iter = infoCandidateApprovalGroup.getCandidates().iterator();
////			System.out.println(infoCandidateApprovalGroup.getSituationName());
////			while(iter.hasNext()){
////				InfoCandidateApproval infoCandidateApproval = (InfoCandidateApproval) iter.next();
////			}	 
////		}
//		
//		request.setAttribute("infoGroup", result);
//		
//		InfoExecutionDegree infoExecutionDegree = null;
//		
//		try {
//			Object args[] = { resultForm.get("executionYear"),  resultForm.get("degree")};
//			infoExecutionDegree = (InfoExecutionDegree) serviceManager.executar(userView, "ReadDegreeByYearAndCode", args);					
//		} catch (ExistingServiceException e) {
//			throw new ExistingActionException(e);
//		}	
//
//		request.setAttribute("infoExecutionDegree", infoExecutionDegree);
//		
//		if (request.getAttribute("confirmation") != null){
//			request.setAttribute("confirmation", request.getAttribute("confirmation"));
//		} else {
//			request.setAttribute("confirmation", "PRINT_PAGE");
//		}
//		return mapping.findForward("PrintReady");	
//	}		
//	 
//	 
//	 
//	 
//	
//	 
//	/**
//	 * @param result
//	 */
//	private void sortLists(List result) {
//		
//		Iterator iterator = result.iterator();
//		while(iterator.hasNext()){
//			InfoCandidateApprovalGroup infoCandidateApprovalGroup = (InfoCandidateApprovalGroup) iterator.next();
//			BeanComparator comparator = null;
//			
//			if (infoCandidateApprovalGroup.getSituationName().equals(SituationName.ADMITIDO_STRING)){
//				comparator = new BeanComparator("candidateName");
//			} else if (infoCandidateApprovalGroup.getSituationName().equals(SituationName.SUPLENTE_STRING)){
//				comparator = new BeanComparator("orderPosition");	
//			} else if (infoCandidateApprovalGroup.getSituationName().equals(SituationName.NAO_ACEITE_STRING)){
//				comparator = new BeanComparator("candidateName");
//			}
//			Collections.sort(infoCandidateApprovalGroup.getCandidates(), comparator);
//		}
//	}
//
//
//	/**
//	 * @param candidateList
//	 * @param ids
//	 * @param remarks
//	 * @param substitutes
//	 * @return
//	 */
//	private List getLists(String[] candidateList, String[] ids, String[] remarks, String[] substitutes, List candidates) {
//		InfoCandidateApprovalGroup approvedList = new InfoCandidateApprovalGroup();
//		approvedList.setSituationName(SituationName.ADMITIDO_STRING);
//		
//		InfoCandidateApprovalGroup notApprovedList = new InfoCandidateApprovalGroup();
//		notApprovedList.setSituationName(SituationName.NAO_ACEITE_STRING);
//		
//		InfoCandidateApprovalGroup substitutesList = new InfoCandidateApprovalGroup();
//		substitutesList.setSituationName(SituationName.SUPLENTE_STRING);
//		
//		
//		
//		for (int i = 0; i < candidateList.length; i++){
//			InfoCandidateApproval infoCandidateApproval = new InfoCandidateApproval();
//			infoCandidateApproval.setCandidateName(((InfoMasterDegreeCandidate) candidates.get(i)).getInfoPerson().getNome());
//			infoCandidateApproval.setIdInternal(new Integer(ids[i]));
//			if ((substitutes[i] != null) && (substitutes[i].length() > 0)){
//				infoCandidateApproval.setOrderPosition(new Integer(substitutes[i]));	
//			} else {
//				infoCandidateApproval.setOrderPosition(null);
//			}
//			
//			infoCandidateApproval.setRemarks(remarks[i]);
//			infoCandidateApproval.setSituationName(candidateList[i]);
//			
//			if((candidateList[i].equals(SituationName.ADMITIDO_STRING)) || 
//				(candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)) ||
//				(candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)) ||
//			   	(candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING))) {
//				approvedList.getCandidates().add(infoCandidateApproval);
//			} else if((candidateList[i].equals(SituationName.SUPLENTE_STRING)) || 
//						(candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)) ||
//						(candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)) ||
//						(candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING))) {
//				substitutesList.getCandidates().add(infoCandidateApproval);
//			} else if (candidateList[i].equals(SituationName.NAO_ACEITE_STRING)){
//				notApprovedList.getCandidates().add(infoCandidateApproval);
//			}
//			
//		}
//
//		List result = new ArrayList();
//		result.add(approvedList);
//		result.add(substitutesList);
//		result.add(notApprovedList);
//		
//		return result;
//	}
//
//
//	private static boolean hasSubstitutes(String[] list){O
//		int size = list.length;
//		int i = 0;
//		for (i=0; i<size; i++){ 
//			if(list[i].equals(SituationName.SUPLENTE_STRING)
//				|| list[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
//				|| list[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
//				|| list[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING))
//				return true;
//		}		 
//		 return false;
//	}
//			 
}
