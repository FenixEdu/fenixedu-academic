package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(
        path = "/caseHandlingStandaloneCandidacyProcess",
        module = "coordinator",
        formBeanClass = net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneCandidacyProcessDA.StandaloneCandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/coordinator/candidacy/standalone/mainCandidacyProcess.jsp", tileProperties = @Tile(
        title = "private.coordinator.management.courses.applicationprocesses.isolatedcurriculum")) })
public class StandaloneCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        throw new RuntimeException("not allowed");
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(CandidacyProcess process, HttpServletRequest request) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final List<IndividualCandidacyProcess> result = new ArrayList(super.getChildProcesses(process, request));
        for (final Iterator<IndividualCandidacyProcess> i = result.iterator(); i.hasNext();) {
            final StandaloneIndividualCandidacyProcess individualCandidacyProcess =
                    (StandaloneIndividualCandidacyProcess) i.next();
            if (!matchesDegree(degreeCurricularPlan, individualCandidacyProcess)) {
                i.remove();
            }
        }
        return result;
    }

    private boolean matchesDegree(final DegreeCurricularPlan degreeCurricularPlan,
            final StandaloneIndividualCandidacyProcess individualCandidacyProcess) {
        if (degreeCurricularPlan == null) {
            return true;
        }
        for (final CurricularCourse curricularCourse : individualCandidacyProcess.getCurricularCourses()) {
            final Degree degree = curricularCourse.getDegree();
            if (degree == degreeCurricularPlan.getDegree()) {
                return true;
            }
        }
        return false;
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
        final String param = request.getParameter("degreeCurricularPlanID");
        return (DegreeCurricularPlan) (param == null || param.isEmpty() ? null : AbstractDomainObject.fromExternalId(param));
    }
}
