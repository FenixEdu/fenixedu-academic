
package ServidorApresentacao.Action.certificate;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoEnrolment;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EnrolmentState;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class PrintCertificateDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);
		String[] destination = null;
		InfoStudent infoStudent = new InfoStudent();
		InfoDegree infoDegree = new InfoDegree();
		InfoPerson infoPerson = new InfoPerson();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		InfoEnrolment infoEnrolment = new InfoEnrolment();
		InfoStudentCurricularPlan newInfoStudentCurricularPlan = new InfoStudentCurricularPlan();
		
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();	
			
			session.removeAttribute(SessionConstants.MATRICULA);
			session.removeAttribute(SessionConstants.MATRICULA_ENROLMENT);
			session.removeAttribute(SessionConstants.DURATION_DEGREE);
			session.removeAttribute(SessionConstants.ENROLMENT);	
			session.removeAttribute(SessionConstants.ENROLMENT_LIST);	
			session.removeAttribute(SessionConstants.APROVMENT);
			
			InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) session.getAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
			
			String certificate = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);	
		    
			if ((certificate.equals("Matrícula")) || (certificate.equals("Matrícula e Inscrição")) || (certificate.equals("Duração do Curso"))){
				if (certificate.equals("Matrícula"))
							session.setAttribute(SessionConstants.MATRICULA, certificate.toUpperCase());
				if (certificate.equals("Matrícula e Inscrição"))
							session.setAttribute(SessionConstants.MATRICULA_ENROLMENT, certificate.toUpperCase());
				if (certificate.equals("Duração do Curso")){
							certificate=new String("Matrícula");
							session.setAttribute(SessionConstants.DURATION_DEGREE, certificate.toUpperCase());
				}			
				
			}else{ 
				
				//get informations
				List enrolmentList = null;				
				if (certificate.equals("Inscrição")){
					Object args[] = {infoStudentCurricularPlan, new EnrolmentState(EnrolmentState.ENROLED)};
					try {
						enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);

					} catch (NonExistingServiceException e) {
						throw new NonExistingActionException("Inscrição", e);
					}
					if (enrolmentList.size() == 0){
						ActionErrors errors = new ActionErrors();
						errors.add("AlunoNãoExiste",
									new ActionError("error.enrolment.notExist"));
						saveErrors(request, errors);
						return new ActionForward(mapping.getInput());
					}
System.out.println("............" + enrolmentList.get(0));
					session.setAttribute(SessionConstants.ENROLMENT, certificate.toUpperCase());
					session.setAttribute(SessionConstants.ENROLMENT_LIST, enrolmentList);	
				}
				if (certificate.equals("Aproveitamento")){
					Object args[] = {infoStudentCurricularPlan, new EnrolmentState(EnrolmentState.APROVED)};
					try {
						enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);

					} catch (NonExistingServiceException e) {
						throw new NonExistingActionException("Inscrição", e);
					}
					if (enrolmentList.size() == 0){
						ActionErrors errors = new ActionErrors();
						errors.add("AlunoNãoExiste",
									new ActionError("error.enrolment.notExist"));
						saveErrors(request, errors);
						return new ActionForward(mapping.getInput());
					}
					session.setAttribute(SessionConstants.APROVMENT, certificate.toUpperCase());
					session.setAttribute(SessionConstants.ENROLMENT_LIST, enrolmentList);	
			
				}
			}	
			
  			session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
  			
  			
			Locale locale = new Locale("pt", "PT");
			Date date = new Date();
			String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

			session.setAttribute(SessionConstants.DATE, formatedDate);
			session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificate);
			
		
		    
		    return mapping.findForward("PrintReady");
			
		  } else
			throw new Exception();   

	}

	  
}
