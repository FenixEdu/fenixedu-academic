/*
 * Created on 20/Mar/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoTeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 * 
 */
public class TeacherLoginAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		
		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoTeacher teacher = null;
		try {
			Object args[] = { userView.getUtilizador()};
			GestorServicos serviceManager = GestorServicos.manager();
			teacher =
				(InfoTeacher) serviceManager.executar(
					userView,
					"ReadTeacherByUsername",
					args);
			//TODO: fix the situation where the teacher is null
			if (teacher==null) {throw new InvalidSessionActionException();		
			}
			List sites = null;
			Object args1[]={teacher};
			sites = (List) serviceManager.executar(
			userView,
			"ReadProfessorships",
			args1);
			
			session.setAttribute(SessionConstants.INFO_TEACHER,teacher);
			session.setAttribute(SessionConstants.INFO_SITES_LIST,sites);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("viewProfessorships");
	}

}
