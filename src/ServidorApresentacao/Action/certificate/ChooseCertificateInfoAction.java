
package ServidorApresentacao.Action.certificate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoPrice;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.ScholarshipNotFinishedActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CertificateList;
import Util.DocumentReason;
import Util.DocumentType;
import Util.GraduationType;
import Util.Specialization;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseCertificateInfoAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			session.removeAttribute(SessionConstants.SPECIALIZATIONS);
			session.removeAttribute(SessionConstants.DOCUMENT_REASON);
			session.removeAttribute(SessionConstants.CERTIFICATE_LIST);
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			ArrayList specializations = Specialization.toArrayList();
			ArrayList documentReason = DocumentReason.toArrayList();
			
			List types = new ArrayList();
			types.add(DocumentType.CERTIFICATE_TYPE);
			
//			 inputs	
			Object args[] = {GraduationType.MASTER_DEGREE_TYPE, types};
			
			// output
			List getCertificateList = null;
//			certificateList = CertificateList.toArrayList();
			try {
				getCertificateList = (List) ServiceManagerServiceFactory.executeService(userView, "ReadCertificateList", args);

			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A lista de certidões", e);
			}


			List result = new ArrayList();
			Iterator iterator = getCertificateList.iterator();
			while(iterator.hasNext()) {	
				InfoPrice price = (InfoPrice) iterator.next(); 
				result.add(price.getDescription());
			}
			session.setAttribute(SessionConstants.DOCUMENT_REASON,documentReason);
			session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
			session.setAttribute(SessionConstants.CERTIFICATE_LIST, new CertificateList().toArrayList());
			
			return mapping.findForward("PrepareReady");
		  } 
			throw new Exception();   

	}

	

	public ActionForward choose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {

			DynaActionForm chooseDeclaration = (DynaActionForm) form;

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			//remove sessions variables
			session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
			session.removeAttribute( SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
			session.removeAttribute( SessionConstants.MASTER_DEGREE_PROOF_HISTORY);
			session.removeAttribute(SessionConstants.DEGREE_TYPE);
			session.removeAttribute(SessionConstants.DATE);
			session.removeAttribute(SessionConstants.DOCUMENT_REASON_LIST);
			session.removeAttribute(SessionConstants.CERTIFICATE_TYPE);	

			
			// Get the Information
			Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
			String graduationType = (String) chooseDeclaration.get("graduationType");
			String certificateString = (String) chooseDeclaration.get("certificateList");
			String[] destination =  (String[]) chooseDeclaration.get("destination");
		    
			
		

			// inputs
			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setNumber(requesterNumber);
			infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
			session.setAttribute(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());
			Object args[] = {infoStudent, new Specialization(graduationType)};
	        
			// output
			InfoStudentCurricularPlan infoStudentCurricularPlan = null;
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
			InfoExecutionYear infoExecutionYear = null;
			
			
			//get informations
			try {
				infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(userView, "CreateDeclaration", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Declaração", e);
			}
			
			if (infoStudentCurricularPlan == null){
				throw new NonExistingActionException("O aluno");
			}
				
		
				
			try {
				if (certificateString.equals( "Fim de curso de Mestrado discriminada com média") || certificateString.equals("Fim de curso de Mestrado simples"))
				{
					Object argsMasterDegreeThesisDataVersion[] = { infoStudentCurricularPlan };
							try
							{
								infoMasterDegreeThesisDataVersion =
									(InfoMasterDegreeThesisDataVersion) ServiceUtils.executeService(
										userView,
										"ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
										argsMasterDegreeThesisDataVersion);
							}
							catch (NonExistingServiceException e)
							{
								throw new NonExistingActionException(
									"O registo da tese ",e);

							}
							
							
					/* * * get master degree proof * * */
					Object argsMasterDegreeProofVersion[] = { infoStudentCurricularPlan };
					try
					{
						infoMasterDegreeProofVersion =
							(InfoMasterDegreeProofVersion) ServiceUtils.executeService(
								userView,
								"ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
								argsMasterDegreeProofVersion);
					}
					catch (NonExistingServiceException e)
					{
						throw new NonExistingActionException(
							"O registo da tese ",
							e);
					}
					catch (ScholarshipNotFinishedServiceException e)
					{
						throw new ScholarshipNotFinishedActionException("",e);
					}
					
				}
					
				infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(userView, "ReadCurrentExecutionYear", null);
	
			} catch (RuntimeException e) {
				throw new RuntimeException("Error", e);
			}
				Locale locale = new Locale("pt", "PT");
				Date date = new Date();
				String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);				
		

				if (destination.length != 0)
					session.setAttribute(SessionConstants.DOCUMENT_REASON_LIST,destination);
					session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
					session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificateString);	
					session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, infoExecutionYear);	
					session.setAttribute(SessionConstants.DATE, formatedDate);	
					session.setAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,infoMasterDegreeThesisDataVersion);
					session.setAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY,infoMasterDegreeProofVersion);
					return mapping.findForward("ChooseSuccess"); 
			
			
		  }
		  throw new Exception();   
	  }

	  
}
