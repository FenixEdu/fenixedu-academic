package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class AddClassesDA
	extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward listClasses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		InfoShift infoShift =
			(InfoShift) request.getAttribute(SessionConstants.SHIFT);

		Object[] args = { infoShift.getIdInternal()};
		List classes = null;
		try {
			classes =
				(List) ServiceUtils.executeService(
					userView,
					"ReadAvailableClassesForShift",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (classes != null && !classes.isEmpty()) {
			Collections.sort(classes, new BeanComparator("nome"));
			request.setAttribute(SessionConstants.CLASSES, classes);
		}

		return mapping.findForward("ListClasses");
	}

	public ActionForward add(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoShift infoShift =
			(InfoShift) request.getAttribute(SessionConstants.SHIFT);

		DynaActionForm addClassesForm = (DynaActionForm) form;
		String[] selectedClasses =
			(String[]) addClassesForm.get("selectedItems");

		List classOIDs = new ArrayList();
		for (int i = 0; i < selectedClasses.length; i++) {
			classOIDs.add(new Integer(selectedClasses[i]));
		}

		Object args[] = { infoShift, classOIDs };
		try {
			ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"AddClassesToShift",
				args);
		} catch (FenixServiceException ex) {
			// No probem, the user refreshed the page after adding classes
			request.setAttribute("selectMultipleItemsForm", null);
			return mapping.getInputForward();
		}

		request.setAttribute("selectMultipleItemsForm", null);

		return mapping.findForward("EditShift");
	}

}