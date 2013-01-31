package net.sourceforge.fenixedu.presentationTier.Action.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/delegatesInfo", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showDegreeDelegates", path = "/student/delegates/showDelegates.jsp", tileProperties = @Tile(
		title = "private.student.view.delegates")) })
public class DelegatesInfoDispatchAction extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		final Person person = getLoggedPerson(request);
		final Student student = person.getStudent();
		final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

		if (student.getLastActiveRegistration() == null) {
			return mapping.findForward("showDegreeDelegates");
		}
		final Degree degree = student.getLastActiveRegistration().getDegree();

		DelegateSearchBean bean = new DelegateSearchBean(currentExecutionYear, degree, degree.getDegreeType());

		return showAll(mapping, request, bean);
	}

	public ActionForward showAll(ActionMapping mapping, HttpServletRequest request, DelegateSearchBean bean) {
		/* DEGREE DELEGATES */
		if (bean.getExecutionYear() != null && bean.getDegree() != null && bean.getDegreeType() != null) {
			request.setAttribute("yearDelegates", getYearDelegateBeans(bean));
			request.setAttribute("degreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_DEGREE));
			request.setAttribute("masterDegreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_MASTER_DEGREE));
			request.setAttribute("integratedMasterDegreeDelegate",
					getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE));
		}
		/* GGAE DELEGATES */
		List<DelegateSearchBean> delegatesFound = getGGAEDelegateBeans();
		Collections.sort(delegatesFound, DelegateSearchBean.DELEGATE_COMPARATOR_BY_EXECUTION_YEAR);

		request.setAttribute("ggaeDelegates", delegatesFound);
		request.setAttribute("searchBean", bean);
		request.setAttribute("searchByDegreeBean", bean);
		return mapping.findForward("showDegreeDelegates");
	}

	private void updateBeanDegree(DelegateSearchBean bean) {
		boolean updated = false;
		Degree first = null;

		for (ExecutionDegree executionDegree : bean.getExecutionYear().getExecutionDegrees()) {
			if (executionDegree.getDegreeType().equals(bean.getDegreeType())) {
				if (!updated) {
					first = executionDegree.getDegree();
					updated = true;
				}
				if (bean.getDegree() != null && executionDegree.getDegree().getName().equals(bean.getDegree().getName())) {
					bean.setDegree(executionDegree.getDegree());
					return;
				}
			}
		}

		bean.setDegree(first);
	}

	private void updateBeanDegreeType(DelegateSearchBean bean) {
		boolean updated = false;
		DegreeType first = bean.getExecutionYear().getExecutionDegrees().get(0).getDegreeType();

		for (ExecutionDegree executionDegree : bean.getExecutionYear().getExecutionDegrees()) {
			if (executionDegree.getDegreeType().getGraduateTitle().split(" ")[0].equals(bean.getDegreeType().getGraduateTitle()
					.split(" ")[0])) {
				bean.setDegreeType(executionDegree.getDegreeType());
				updated = true;
				break;
			}
		}

		if (!updated) {
			bean.setDegreeType(first);
		}

		updateBeanDegree(bean);
	}

	public ActionForward updateDelegates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
		return showAll(mapping, request, bean);
	}

	public ActionForward updateExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
		RenderUtils.invalidateViewState();
		updateBeanDegreeType(bean);
		return showAll(mapping, request, bean);
	}

	public ActionForward updateDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
		RenderUtils.invalidateViewState();
		updateBeanDegree(bean);
		return showAll(mapping, request, bean);
	}

	/*
	 * AUXILIARY METHODS
	 */

	private Degree getDefaultDegreeGivenDegreeType(DegreeType degreeType) {
		List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
		return degrees.get(0);
	}

	/* Delegates from given degree (not year delegates) */
	private DelegateSearchBean getDelegateSearchBean(DelegateSearchBean bean, FunctionType functionType) {
		List<Student> delegates = new ArrayList<Student>();
		if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
			delegates.addAll(bean.getDegree().getAllActiveDelegatesByFunctionType(functionType, bean.getExecutionYear()));
		} else {
			delegates.addAll(bean.getDegree()
					.getAllDelegatesByExecutionYearAndFunctionType(bean.getExecutionYear(), functionType));
		}
		return (delegates.isEmpty() ? null : new DelegateSearchBean(delegates.get(0).getPerson(), functionType,
				bean.getExecutionYear()));
	}

	/* Year delegates from given degree */
	private List<DelegateSearchBean> getYearDelegateBeans(DelegateSearchBean bean) {
		List<DelegateSearchBean> yearDelegates = new ArrayList<DelegateSearchBean>();
		final Degree degree = bean.getDegree();
		final ExecutionYear executionYear = bean.getExecutionYear();

		for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
			final CurricularYear curricularYear = CurricularYear.readByYear(i);
			Student student = null;

			student = degree.getYearDelegateByExecutionYearAndCurricularYear(executionYear, curricularYear);

			if (student != null) {
				DelegateSearchBean delegateBean =
						new DelegateSearchBean(student.getPerson(), FunctionType.DELEGATE_OF_YEAR, curricularYear, executionYear);
				yearDelegates.add(delegateBean);
			}
		}
		return yearDelegates;
	}

	/* Delegates from all degrees */
	private List<DelegateSearchBean> getGGAEDelegateBeans() {
		final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
		List<DelegateSearchBean> result = new ArrayList<DelegateSearchBean>();

		Set<Function> functions = Function.readAllActiveFunctionsByType(FunctionType.DELEGATE_OF_GGAE);
		for (Function function : functions) {
			for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(currentExecutionYear)) {
				DelegateSearchBean bean = new DelegateSearchBean(personFunction.getPerson(), personFunction);
				result.add(bean);
			}
		}

		return result;
	}
}
