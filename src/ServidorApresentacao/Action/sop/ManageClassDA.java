package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoClass;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageClassDA
	extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoClass infoClass =
			(InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

		// Fill out the form with the name of the class
		DynaActionForm classForm = (DynaActionForm) form;
		classForm.set("className", infoClass.getNome());

		//Get list of shifts and place them in request
		Object args[] = { infoClass };

		List infoShifts =
			(List) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadShiftsByClass",
				args);

		/* Sort the list of shifts */
		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(
			new BeanComparator("infoDisciplinaExecucao.nome"));
		chainComparator.addComparator(new BeanComparator("tipo.tipo"));
		chainComparator.addComparator(new BeanComparator("nome"));
		Collections.sort(infoShifts, chainComparator);

		/* Place list of shifts in request */
		request.setAttribute(SessionConstants.SHIFTS, infoShifts);

		return mapping.findForward("EditClass");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaValidatorForm classForm = (DynaValidatorForm) form;

		String className = (String) classForm.get("className");

		IUserView userView = SessionUtils.getUserView(request);

		InfoClass infoClassOld =
			(InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

		InfoClass infoClassNew = new InfoClass();
		infoClassNew.setNome(className);
		infoClassNew.setAnoCurricular(infoClassOld.getAnoCurricular());
		infoClassNew.setIdInternal(infoClassOld.getIdInternal());
		infoClassNew.setInfoExecutionDegree(
			infoClassOld.getInfoExecutionDegree());
		infoClassNew.setInfoExecutionPeriod(
			infoClassOld.getInfoExecutionPeriod());

		Object argsCriarTurma[] = { infoClassOld, infoClassNew };

		try {
			ServiceUtils.executeService(
				userView,
				"EditarTurma",
				argsCriarTurma);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("A Turma", e);
		}

		request.removeAttribute(SessionConstants.CLASS_VIEW);
		request.setAttribute(SessionConstants.CLASS_VIEW, infoClassNew);

		return prepare(mapping, form, request, response);
	}

}