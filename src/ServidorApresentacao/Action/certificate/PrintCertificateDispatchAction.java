
package ServidorApresentacao.Action.certificate;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

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
		
		if (session != null) {

			session.removeAttribute(SessionConstants.MATRICULA);
			session.removeAttribute(SessionConstants.MATRICULA_ENROLMENT);
			session.removeAttribute(SessionConstants.DURATION_DEGREE);
			session.removeAttribute(SessionConstants.ENROLMENT);

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
				
			} 
	
			// para os restantes ir a base de dados ler os respectivos dados
			//e por as respectivas variaveis em sessao
			if (certificate.equals("Inscrição")){
				
				//get enrolment information 
				
				session.setAttribute(SessionConstants.ENROLMENT, certificate.toUpperCase());
			}
	
			
			
			String nome = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getNome();
			String nomePai = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getNomePai();
			String nomeMae = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getNomeMae();
			String nacionalidade = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getNacionalidade();
			String naturalidade = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getFreguesiaNaturalidade();
			String curso = infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
			
			infoPerson.setNome(nome.toUpperCase());
			infoPerson.setNomePai(nomePai.toUpperCase());
			infoPerson.setNomeMae(nomeMae.toUpperCase());
			infoPerson.setNacionalidade(nacionalidade.toUpperCase());
			infoPerson.setFreguesiaNaturalidade(naturalidade.toUpperCase());
			
			infoStudent.setInfoPerson(infoPerson);
			infoStudent.setNumber(infoStudentCurricularPlan.getInfoStudent().getNumber());
			infoStudentCurricularPlan.setInfoStudent(infoStudent);
			
			infoDegree.setNome(curso.toUpperCase());
			infoDegreeCurricularPlan.setInfoDegree(infoDegree);
			infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
			
			
  			session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
  			
  			
			Locale locale = new Locale("pt", "PT");
			Date date = new Date();
			String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

			session.setAttribute(SessionConstants.DATE, formatedDate);
			session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificate.toUpperCase());
			
		
		    
		    return mapping.findForward("PrintReady");
			
		  } else
			throw new Exception();   

	}

	  
}
