package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.OwnCourseVigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListVigilanciesForEvaluationDispatchAction extends FenixDispatchAction {

	private void setState(HttpServletRequest request) {
		String executionCourseID = request.getParameter("executionCourseID");
		request.setAttribute("executionCourseID", executionCourseID);
		String writtenEvaluationID = request.getParameter("evaluationOID");
		WrittenEvaluation evaluation = (WrittenEvaluation) RootDomainObject.readDomainObjectByOID(
				WrittenEvaluation.class, Integer.valueOf(writtenEvaluationID));

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(Vigilancy.COMPARATOR_BY_VIGILANT_CATEGORY);
		comparator.addComparator(Vigilancy.COMPARATOR_BY_VIGILANT_USERNAME);

		List<Vigilancy> vigilancies = new ArrayList<Vigilancy>(evaluation.getOthersVigilancies());
		Collections.sort(vigilancies, comparator);
		List<Vigilancy> ownVigilancies = new ArrayList<Vigilancy>(evaluation.getTeachersVigilancies());
		Collections.sort(ownVigilancies, comparator);

		request.setAttribute("evaluation", evaluation);
		request.setAttribute("ownVigilancies", ownVigilancies);
		request.setAttribute("vigilancies", vigilancies);
	}

	public ActionForward viewVigilants(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		setState(request);
		request.setAttribute("unconvokeRequest", new VariantBean());
		return mapping.findForward("listVigilantsForEvaluation");

	}

	public ActionForward editReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		setState(request);
		return mapping.findForward("editReportForEvaluation");
	}

	public ActionForward changeConvokeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		String vigilancyID = request.getParameter("oid");
		Vigilancy vigilancy = (Vigilancy) RootDomainObject.readDomainObjectByOID(Vigilancy.class,
				Integer.valueOf(vigilancyID));
		String participationType = request.getParameter("participationType");
		AttendingStatus status = AttendingStatus.valueOf(participationType);
		try {
			Object[] args = { vigilancy, status };
			executeService("ChangeConvokeStatus", args);
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
		}

		return editReport(mapping, form, request, response);
	}

	public ActionForward changeActiveConvoke(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		String vigilancyID = request.getParameter("oid");
		Vigilancy vigilancy = (Vigilancy) RootDomainObject.readDomainObjectByOID(Vigilancy.class,
				Integer.valueOf(vigilancyID));
		if (vigilancy instanceof OwnCourseVigilancy) {
			String bool = request.getParameter("bool");
			Boolean active = Boolean.valueOf(bool);
			try {
				Object[] args = { vigilancy, active, getLoggedPerson(request) };
				executeService(request, "ChangeConvokeActive", args);
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}
		return viewVigilants(mapping, form, request, response);
	}

	public ActionForward requestUnconvokes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		IViewState viewState = RenderUtils.getViewState("variantBean");
		WrittenEvaluation evaluation = (WrittenEvaluation) RootDomainObject.readDomainObjectByOID(
				WrittenEvaluation.class, Integer.valueOf(request.getParameter("evaluationOID")));
		if (viewState != null) {
			List<Vigilancy> vigilancies = evaluation.getActiveOtherVigilancies();
			VariantBean bean = (VariantBean) viewState.getMetaObject().getObject();
			Integer numberOfVigilantsToUnconvoke = bean.getInteger();
			if (numberOfVigilantsToUnconvoke > vigilancies.size()) {
				addActionMessage(request, "error.there.are.not.so.many.vigilants");
				return viewVigilants(mapping, form, request, response);
			}

			Collections.sort(vigilancies, new ReverseComparator(
					Vigilancy.COMPARATOR_BY_VIGILANT_SORT_CRITERIA));

			for (Vigilancy vigilancy : vigilancies.subList(0, numberOfVigilantsToUnconvoke)) {
				try {
					Object[] args = { vigilancy, Boolean.FALSE, getLoggedPerson(request) };
					executeService(request, "ChangeConvokeActive", args);
				} catch (DomainException e) {
					addActionMessage(request, e.getMessage());
				}

			}
		}

		RenderUtils.invalidateViewState("variantBean");
		return viewVigilants(mapping, form, request, response);
	}

}
