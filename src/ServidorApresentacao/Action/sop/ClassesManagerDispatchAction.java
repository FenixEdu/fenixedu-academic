/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 3/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ClassesManagerDispatchAction extends DispatchAction {
	static public final String CLASS_LIST_KEY = "classesList";

	public ActionForward listClasses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		/**
		 * obter o serviço e listar as turmas do contexto em sessão.
		 * */
		GestorServicos gestor = GestorServicos.manager();
		Object argsLerTurmas[] = { SessionUtils.getContext(request)};

		List classesList =
			(List) gestor.executar(
				SessionUtils.getUserView(request),
				"LerTurmas",
				argsLerTurmas);
		if (classesList != null && !classesList.isEmpty())
			request.setAttribute(CLASS_LIST_KEY, classesList);

		return mapping.findForward("listClasses");
	}

}
