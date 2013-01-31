package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.graduatedPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		path = "/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess",
		module = "coordinator",
		formBeanClass = FenixActionForm.class)
@Forwards({
		@Forward(
				name = "intro",
				path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=listProcessAllowedActivities"),
		@Forward(
				name = "introduce-candidacy-result",
				path = "/coordinator/candidacy/graduatedPerson/introduceCandidacyResult.jsp",
				tileProperties = @Tile(title = "private.coordinator.management.courses.applicationprocesses.graduates")),
		@Forward(
				name = "list-allowed-activities",
				path = "/coordinator/candidacy/graduatedPerson/listIndividualCandidacyActivities.jsp",
				tileProperties = @Tile(title = "private.coordinator.management.courses.applicationprocesses.graduates")) })
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA
		extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessDA {

	private DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean getCandidacyResultBean() {
		return getRenderedObject("individualCandidacyResultBean");
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CoordinatedDegreeInfo.setCoordinatorContext(request);
		return super.execute(mapping, actionForm, request, response);
	}

	@Override
	public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final Integer degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
		request.setAttribute("seriesGrade", getProcess(request).getCandidacy()
				.getDegreeCandidacyForGraduatedPersonSeriesGadeForDegree(degreeCurricularPlan.getDegree()));
		return super.listProcessAllowedActivities(mapping, form, request, response);
	}

	@Override
	public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {
		final Integer degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
		request.setAttribute("individualCandidacyResultBean", new DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(
				getProcess(request), degreeCurricularPlan.getDegree()));
		return mapping.findForward("introduce-candidacy-result");
	}

}
