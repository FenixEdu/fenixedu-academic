/*
 * Created on Feb 18, 2004
 *
 */
package ServidorApresentacao.Action.teacher;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoBranch;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class TfcManagementAction extends FenixDispatchAction
{
	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		DynaActionForm tfcForm = (DynaActionForm) form;
		String nomeResponsavel = (String) tfcForm.get("responsableTeacherName");
		String nomeCoResponsavel = (String) tfcForm
				.get("coResponsableTeacherName");

		System.out.println("O nome do prof é: " + nomeResponsavel);
		System.out.println("O nome do co-prof é: " + nomeCoResponsavel);

		Enumeration parametros = request.getParameterNames();
		while(parametros.hasMoreElements())
			System.out.println("O nome do elemento é: " + parametros.nextElement());
		
		return mapping.findForward("");
	}

	public ActionForward prepareTfcInformation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException,
			FenixServiceException
	{

		IUserView userView = SessionUtils.getUserView(request);

		Object args[] = {userView.getUtilizador()};

		InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(
				userView, "ReadTeacherByUsername", args);

		request.setAttribute("infoTeacher", infoTeacher);

		List lista = infoTeacher.getResponsibleForExecutionCourses();

		System.out.println("Responsavel por:");
		if (lista != null)
		{
			for (int i = 0; i < lista.size(); i++)
				System.out.println(lista.get(i));
		}
		
		Integer degreeCurricularPlanId = new Integer(48);
		Object[] args1 = {degreeCurricularPlanId};
		List branches = null;
		try
		{
			branches = (List) ServiceUtils.executeService(userView,
					"ReadBranchesByDegreeCurricularPlanId", args1);
		}
		catch (FenixServiceException fse)
		{
			throw new FenixActionException(fse);
		}
		
		Iterator iterator = branches.iterator();
		while(iterator.hasNext())
		{
			InfoBranch infoBranch = (InfoBranch)iterator.next();
			System.out.println("O Raio do nome do Ramo é: " + infoBranch.getName());
		}
	
		System.out.println("A Lista tem o tamanho: " + branches.size());

		request.setAttribute("branchList",branches);		
		return mapping.findForward("submitTfcProposal");

	}
}
