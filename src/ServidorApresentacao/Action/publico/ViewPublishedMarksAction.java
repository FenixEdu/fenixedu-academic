package ServidorApresentacao.Action.publico;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Fernanda Quitério
 * 
 */
public class ViewPublishedMarksAction extends FenixDispatchAction {

	public ActionForward viewPublishedMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer objectCode = getFromRequest("objectCode", request);
		Integer examCode = getFromRequest("examCode", request);

		Object[] args = { objectCode, examCode };
		GestorServicos gestorServicos = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;
		try {
			siteView = (TeacherAdministrationSiteView) gestorServicos.executar(null, "ReadPublishedMarksByExam", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		Collections.sort(infoSiteMarks.getMarksList(), new BeanComparator("infoFrequenta.aluno.number"));

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("viewPublishedMarks");
	}
	
	private Integer getFromRequest(String parameter, HttpServletRequest request) {
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterCode = new Integer(parameterCodeString);
		}
		return parameterCode;

	}
}