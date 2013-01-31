package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author tfc130
 */
@Mapping(
		module = "resourceAllocationManager",
		path = "/escolherContextoForm",
		input = "/prepararEscolherContexto.do",
		attribute = "escolherContextoForm",
		formBean = "escolherContextoForm",
		scope = "request")
@Forwards(value = { @Forward(name = "Licenciatura execucao inexistente", path = "/naoExecutado.do"),
		@Forward(name = "Sucesso", path = "/gestaoHorarios.do") })
@Exceptions(value = { @ExceptionHandling(
		type = net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente.class,
		key = "ServidorAplicacao.Servico.ExcepcaoInexistente",
		handler = org.apache.struts.action.ExceptionHandler.class,
		path = "/resourceAllocationManager/paginaPrincipal.do",
		scope = "request") })
public class EscolherContextoFormAction extends FenixContextAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		super.execute(mapping, form, request, response);

		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		IUserView userView = UserView.getUser();

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		Integer semestre = infoExecutionPeriod.getSemester();
		Integer anoCurricular = (Integer) escolherContextoForm.get("anoCurricular");

		InfoCurricularYear infoCurricularYear = ReadCurricularYearByOID.run(anoCurricular);

		int index = Integer.parseInt((String) escolherContextoForm.get("index"));

		request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);

		List infoExecutionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index);

		if (infoExecutionDegree != null) {
			CurricularYearAndSemesterAndInfoExecutionDegree cYSiED =
					new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, infoExecutionDegree);
			request.setAttribute(PresentationConstants.CONTEXT_KEY, cYSiED);

			request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
		} else {

			return mapping.findForward("Licenciatura execucao inexistente");
		}
		return mapping.findForward("Sucesso");
	}
}