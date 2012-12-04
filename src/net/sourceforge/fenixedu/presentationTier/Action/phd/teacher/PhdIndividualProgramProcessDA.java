package net.sourceforge.fenixedu.presentationTier.Action.phd.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
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

@Mapping(path = "/phdIndividualProgramProcess", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

	@Forward(name = "manageProcesses", path = "/phd/teacher/manageProcesses.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "viewProcess", path = "/phd/teacher/viewProcess.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "viewInactiveProcesses", path = "/phd/teacher/viewInactiveProcesses.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "searchResults", path = "/phd/teacher/searchResults.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "viewAlertMessages", path = "/phd/teacher/viewAlertMessages.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.alertmessages")),
	@Forward(name = "viewAlertMessageArchive", path = "/phd/teacher/viewAlertMessageArchive.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.alertmessages")),
	@Forward(name = "viewAlertMessage", path = "/phd/teacher/viewAlertMessage.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.alertmessages")),
	@Forward(name = "viewProcessAlertMessages", path = "/phd/teacher/viewProcessAlertMessages.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.alertmessages")),
	@Forward(name = "viewProcessAlertMessageArchive", path = "/phd/teacher/viewProcessAlertMessageArchive.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.alertmessages")),
	@Forward(name = "requestPublicPresentationSeminarComission", path = "/phd/teacher/requestPublicPresentationSeminarComission.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "exemptPublicPresentationSeminarComission", path = "/phd/teacher/exemptPublicPresentationSeminarComission.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "manageGuidanceDocuments", path = "/phd/teacher/manageGuidanceDocuments.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")),
	@Forward(name = "uploadGuidanceDocument", path = "/phd/teacher/uploadGuidanceDocument.jsp", tileProperties = @Tile(title = "private.teacher.doctorates.phdprocesses")) })
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

	final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setFilterPhdPrograms(false);

	final List<PhdIndividualProgramProcess> processes = new ArrayList<PhdIndividualProgramProcess>();
	for (final InternalPhdParticipant participant : getLoggedPerson(request).getInternalParticipants()) {
	    processes.add(participant.getIndividualProcess());
	}

	searchBean.setProcesses(processes);

	return searchBean;

    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return forwardToViewProcess(mapping, request);
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

    @Override
    public ActionForward prepareRequestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ActionForward forward = super.prepareRequestPublicPresentationSeminarComission(mapping, form, request, response);

	PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) request
		.getAttribute("requestPublicPresentationSeminarComissionBean");
	bean.setGenerateAlert(true);

	return forward;
    }
}
