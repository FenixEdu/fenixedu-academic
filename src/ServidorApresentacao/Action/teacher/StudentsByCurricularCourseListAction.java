package ServidorApresentacao.Action.teacher;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoSiteStudents;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Fernanda Quitério
 * @author Tânia Pousão 
 * @author Ângela
 *
 */
public class StudentsByCurricularCourseListAction extends DispatchAction {

	public ActionForward readStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession();

		Integer objectCode = null;
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		objectCode = new Integer(objectCodeString);

		Integer scopeCode = null;
		String scopeCodeString = request.getParameter("scopeCode");
		if (scopeCodeString == null) {
			scopeCodeString = (String) request.getAttribute("scopeCode");
		}
		if (scopeCodeString != null) {
			scopeCode = new Integer(scopeCodeString);
		}

		UserView userView = (UserView) session.getAttribute("UserView");

		Object args[] = { objectCode, scopeCode};

		GestorServicos gestor = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;
		try {
			siteView = (TeacherAdministrationSiteView) gestor.executar(userView, "ReadStudentsByCurricularCourse", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
		Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("success");
	}
}
