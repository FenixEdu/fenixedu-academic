
package ServidorApresentacao.Action.certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

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
		if (session != null) {
			if (SessionConstants.DOCUMENT_REASON_LIST != null)
				destination = (String[]) session.getAttribute(SessionConstants.DOCUMENT_REASON_LIST);
				
			InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) session.getAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
			String certificate = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);	
			
			if ((certificate.equals("Matrícula")) && (certificate.equals("Matrícula e Inscrição")));
					//por em sessao o tipo de certificado a imprimir
			
					
		    // para os restantes ir a base de dados ler os respectivos dados
		    //e por as respectivas variaveis em sessao
		    
		    return mapping.findForward("PrintReadyMatricula");
			
		  } else
			throw new Exception();   

	}

	  
}
