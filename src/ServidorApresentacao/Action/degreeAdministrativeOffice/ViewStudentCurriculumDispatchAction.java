package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author Fernanda Quitério 05/Fev/2004
 *  
 */
public class ViewStudentCurriculumDispatchAction extends DispatchAction
{

	public ActionForward prepareView(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("prepareViewStudentCurriculumChooseStudent");
	}
}