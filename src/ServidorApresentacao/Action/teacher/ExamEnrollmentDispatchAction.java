/*
 * Created on 28/Mai/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExamEnrollment;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Nunes
 *
 */
public class ExamEnrollmentDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEditExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaValidatorForm examEnrollmentForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal =
			(Integer) request.getAttribute("examIdInternal");
		Integer disciplinaExecucaoIdInternal =
			(Integer) request.getAttribute("disciplinaExecucaoIdInternal");

		Object args[] = { disciplinaExecucaoIdInternal, examIdInternal };

		InfoExamEnrollment infoExamEnrollment =
			(InfoExamEnrollment) ServiceUtils.executeService(userView, "ReadExamEnrollment", args);
		if (infoExamEnrollment!= null){
			request.setAttribute("infoExamEnrollment", infoExamEnrollment);
					return mapping.findForward("editEnrollment");
		}else{
			
					return mapping.findForward("createEnrollment");
		}
		
	}

	// Isto está para fazer, tive de ir fazer o serviço de leitura. 

	public ActionForward editExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaValidatorForm examEnrollmentForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);

		List infoExamEnrollmentList =
			(List) request.getAttribute("infoExamEnrollment");

		Iterator iterator = infoExamEnrollmentList.listIterator();
		/*
				InfoExamEnrollment infoExamEnrollmentToWrite =
					(InfoExamEnrollment) iterator.next();
		
		*/

		return mapping.findForward("showInfoExamEnrollment");
	}

}
