/*
 * Created on 25/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TeacherManagerDispatchAction extends FenixDispatchAction {

	public ActionForward viewTeachersByProfessorship(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		try {
			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoSite infoSite =
				(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			Object args[] = { infoSite.getInfoExecutionCourse()};
			GestorServicos serviceManager = GestorServicos.manager();
			List teachers =
				(List) serviceManager.executar(
					userView,
					"ReadTeachersByExecutionCourseProfessorship",
					args);
			if (teachers!=null && !teachers.isEmpty()){
			session.setAttribute(
				SessionConstants.TEACHERS_LIST,
			teachers);}
			return mapping.findForward("viewTeachers");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		} 

	}

}
