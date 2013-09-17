package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.degreeTransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "coordinator",
        formBeanClass = FenixActionForm.class)
@Forwards({
        @Forward(name = "intro", path = "/caseHandlingDegreeTransferCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "introduce-candidacy-result", path = "/coordinator/candidacy/degreeTransfer/introduceCandidacyResult.jsp"),
        @Forward(name = "list-allowed-activities",
                path = "/coordinator/candidacy/degreeTransfer/listIndividualCandidacyActivities.jsp") })
public class DegreeTransferIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer.DegreeTransferIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("seriesGrade", getProcess(request).getCandidacy()
                .getDegreeTransferIndividualCandidacySeriesGradeForDegree(degreeCurricularPlan.getDegree()));
        return super.listProcessAllowedActivities(mapping, form, request, response);
    }

    @Override
    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final String degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("individualCandidacyResultBean", new DegreeTransferIndividualCandidacyResultBean(
                getProcess(request), degreeCurricularPlan.getDegree()));
        return mapping.findForward("introduce-candidacy-result");
    }
}
