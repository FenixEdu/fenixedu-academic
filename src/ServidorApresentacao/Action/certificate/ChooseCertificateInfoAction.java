
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
import DataBeans.InfoPrice;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CertificateList;
import Util.DocumentReason;
import Util.DocumentType;
import Util.GraduationType;
import Util.Specialization;
import Util.TipoCurso;

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
		GestorServicos serviceManager = GestorServicos.manager();	


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
				getCertificateList = (List) serviceManager.executar(userView, "ReadCertificateList", args);

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
		  } else
			throw new Exception();   

	}

	

	public ActionForward choose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {

			DynaActionForm chooseDeclaration = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();	
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			//remove sessions variables
			session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
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
			InfoExecutionYear infoExecutionYear = null;
			
			
			//get informations
			try {
				infoStudentCurricularPlan = (InfoStudentCurricularPlan) serviceManager.executar(userView, "CreateDeclaration", args);

			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Declaração", e);
			}
			
			if (infoStudentCurricularPlan == null){
				throw new NonExistingActionException("O aluno");
			}
				
			else {
				try {
					infoExecutionYear = (InfoExecutionYear) serviceManager.executar(userView, "ReadCurrentExecutionYear", null);
	
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
				return mapping.findForward("ChooseSuccess"); 
			}
			
		  } else
		  throw new Exception();   
	  }

	  
}
