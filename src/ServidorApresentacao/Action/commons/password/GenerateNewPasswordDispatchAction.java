package ServidorApresentacao.Action.commons.password;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoPerson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RandomStringGenerator;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */

public class GenerateNewPasswordDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();


		DynaActionForm newPasswordForm = (DynaActionForm) form;

//		newPasswordForm.set("userType", null);
//		newPasswordForm.set("number", null);
		
		ArrayList userTypes = new ArrayList();
		userTypes.add(new LabelValueBean("Docente", "D"));
		userTypes.add(new LabelValueBean("Funcionário", "F"));
		userTypes.add(new LabelValueBean("Aluno de Mestrado", "M"));
		userTypes.add(new LabelValueBean("Aluno de Licenciatura", "L"));

		request.setAttribute("userTypes", userTypes);

		return mapping.findForward("PrepareSuccess");
	}
	
	
	public ActionForward findPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		GestorServicos serviceManager = GestorServicos.manager();
			
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			

		DynaActionForm newPasswordForm = (DynaActionForm) form;

		String userType = (String) newPasswordForm.get("userType");
		String personNumberString = (String) newPasswordForm.get("number");


	  
		InfoPerson infoPerson = null;
		try {
			Object args[] = { new String(userType + personNumberString) };
			infoPerson = (InfoPerson) serviceManager.executar(userView, "ReadPersonByUsername", args);
		} catch (ExcepcaoInexistente e) {
			throw new NonExistingActionException("A Pessoa", e);
		}

		request.setAttribute("infoPerson", infoPerson);


		return mapping.findForward("Confirm");
	}


	public ActionForward generatePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		GestorServicos serviceManager = GestorServicos.manager();
			
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			


		Integer personID = new Integer(request.getParameter("personID"));




		String password = RandomStringGenerator.getRandomStringGenerator(8);
		
	
		// Change the Password
		try {
			Object args[] = {personID , password};
			serviceManager.executar(userView, "ChangePersonPassword", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}

		request.setAttribute("password", password);

		InfoPerson infoPerson = null;
		try {
			Object args[] = { request.getParameter("username") };
			infoPerson = (InfoPerson) serviceManager.executar(userView, "ReadPersonByUsername", args);
		} catch (ExcepcaoInexistente e) {
			throw new NonExistingActionException("A Pessoa", e);
		}


		request.setAttribute("infoPerson", infoPerson);


		return mapping.findForward("Success");
	}


}