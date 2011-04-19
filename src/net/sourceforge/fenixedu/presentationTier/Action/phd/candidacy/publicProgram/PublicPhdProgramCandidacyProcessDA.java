package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public abstract class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	if (bean != null && bean.hasCandidacyHashCode()) {
	    canEditCandidacy(request, bean.getCandidacyHashCode());
	}

	ActionForward forward = filterDispatchMethod(bean, mapping, actionForm, request, response);

	if (forward != null) {
	    return forward;
	}

	return super.execute(mapping, actionForm, request, response);
    }

    protected abstract ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected PhdProgramCandidacyProcessBean getCandidacyBean() {
	return getRenderedObject("candidacyBean");
    }

    protected void canEditCandidacy(final HttpServletRequest request, final PhdProgramPublicCandidacyHashCode hashCode) {
	request.setAttribute("canEditCandidacy", !isValidatedByCandidate(hashCode));
    }

    protected boolean isValidatedByCandidate(final PhdProgramPublicCandidacyHashCode hashCode) {
	return hashCode.hasPhdProgramCandidacyProcess() && hashCode.getIndividualProgramProcess().isValidatedByCandidate();
    }

    protected void canEditPersonalInformation(final HttpServletRequest request, final Person person) {
	if (person.hasRole(RoleType.EMPLOYEE)) {
	    request.setAttribute("canEditPersonalInformation", false);
	    addWarningMessage(request, "message.employee.data.must.be.updated.in.human.resources.section");
	} else if (person.hasAnyPersonRoles() || person.hasUser() || person.hasStudent()) {
	    request.setAttribute("canEditPersonalInformation", false);
	    addWarningMessage(request, "message.existing.person.data.must.be.updated.in.academic.office");
	} else {
	    request.setAttribute("canEditPersonalInformation", true);
	}
    }

    protected void addValidationMessage(final HttpServletRequest request, final String key, final String... args) {
	addActionMessage("validation", request, key, args);
    }

    protected IUserView createMockUserView(final Person person) {
	return new MockUserView(null, Collections.EMPTY_LIST, person);
    }

}
