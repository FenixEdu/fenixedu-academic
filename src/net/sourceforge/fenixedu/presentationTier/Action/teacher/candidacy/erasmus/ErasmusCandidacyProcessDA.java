package net.sourceforge.fenixedu.presentationTier.Action.teacher.candidacy.erasmus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/caseHandlingMobilityApplicationProcess",
		module = "teacher",
		formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp") })
public class ErasmusCandidacyProcessDA extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusCandidacyProcessDA {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		setChooseDegreeBean(request);
		ActionForward forward = super.execute(mapping, actionForm, request, response);
		setChooseDegreeBean(request);
		request.setAttribute("chooseDegreeBeanSchemaName", "ErasmusChooseDegreeBean.coordinator.selectDegree");
		return forward;
	}

	@Override
	protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
		Collection<IndividualCandidacyProcess> processes = super.getChildProcesses(process, request);

		List<IndividualCandidacyProcess> result = new ArrayList<IndividualCandidacyProcess>();

		CollectionUtils.select(processes, new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				IndividualCandidacyProcess child = (IndividualCandidacyProcess) arg0;

				return ((MobilityApplicationProcess) process).getDegreesAssociatedToTeacherAsCoordinator(getTeacher()).contains(
						((MobilityIndividualApplicationProcess) child).getCandidacy().getSelectedDegree());
			}

		}, result);

		return result;
	}

	private Teacher getTeacher() {
		return AccessControl.getPerson().getTeacher();
	}

	public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
		final Integer degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

		if (degreeCurricularPlanOID != null) {
			return rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
		}

		return null;
	}

	public static class ErasmusCandidacyDegreesProvider implements DataProvider {

		@Override
		public Object provide(Object source, Object currentValue) {
			ChooseDegreeBean bean = (ChooseDegreeBean) source;

			Teacher teacher = AccessControl.getPerson().getTeacher();
			return ((MobilityApplicationProcess) bean.getCandidacyProcess()).getDegreesAssociatedToTeacherAsCoordinator(teacher);
		}

		@Override
		public Converter getConverter() {
			return new DomainObjectKeyConverter();
		}

	}

	@Override
	public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		throw new DomainException("error.permission.denied");
	}

}
