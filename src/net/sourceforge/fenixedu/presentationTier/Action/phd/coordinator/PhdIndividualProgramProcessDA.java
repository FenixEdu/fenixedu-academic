package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdCandidacyPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdInactivePredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdSeminarPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdThesisPredicateContainer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),

@Forward(name = "viewInactiveProcesses", path = "/phd/coordinator/viewInactiveProcesses.jsp"),

@Forward(name = "searchResults", path = "/phd/coordinator/searchResults.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp"),

@Forward(name = "viewCurriculum", path = "/phd/coordinator/viewCurriculum.jsp"),

@Forward(name = "manageEnrolments", path = "/phd/coordinator/manageEnrolments.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    private static final PredicateContainer<?>[] CANDIDACY_CATEGORY = { PhdCandidacyPredicateContainer.DELIVERED,
	    PhdCandidacyPredicateContainer.PENDING, PhdCandidacyPredicateContainer.APPROVED,
	    PhdCandidacyPredicateContainer.CONCLUDED };

    private static final PredicateContainer<?>[] SEMINAR_CATEGORY = { PhdSeminarPredicateContainer.SEMINAR_PROCESS_STARTED,
	    PhdSeminarPredicateContainer.AFTER_FIRST_SEMINAR_REUNION };

    private static final PredicateContainer<?>[] THESIS_CATEGORY = { PhdThesisPredicateContainer.PROVISIONAL_THESIS_DELIVERED,
	    PhdThesisPredicateContainer.DISCUSSION_SCHEDULED };

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setPhdPrograms(getManagedPhdPrograms(request));
	searchBean.setFilterPhdProcesses(false);
	return searchBean;
    }

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
	final Set<PhdProgram> result = new HashSet<PhdProgram>();
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	for (final Coordinator coordinator : getLoggedPerson(request).getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()
		    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
		result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
	    }
	}

	return result;
    }

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");

	if (bean == null) {
	    bean = new ManageEnrolmentsBean();
	    bean.setProcess(process);
	    bean.setSemester(ExecutionSemester.readActualExecutionSemester());
	}

	filter(bean, process);

	request.setAttribute("manageEnrolmentsBean", bean);
	return mapping.findForward("manageEnrolments");
    }

    private void filter(ManageEnrolmentsBean bean, PhdIndividualProgramProcess process) {
	final StudentCurricularPlan scp = process.getRegistration().getLastStudentCurricularPlan();
	bean.setEnrolments(scp.getEnrolmentsByExecutionPeriod(bean.getSemester()));
    }

    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
	return PhdInactivePredicateContainer.CONCLUDED_THIS_YEAR;
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
	return Arrays.asList(THESIS_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
	return Arrays.asList(SEMINAR_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
	return Arrays.asList(CANDIDACY_CATEGORY);
    }
}
