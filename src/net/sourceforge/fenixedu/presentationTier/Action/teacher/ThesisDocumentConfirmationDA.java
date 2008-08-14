package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ThesisDocumentConfirmationDA extends FenixDispatchAction {

    public static class ThesisPresentationWrapper {

	public static final Comparator<ThesisPresentationWrapper> COMPARATOR_BY_STUDENT = new Comparator<ThesisPresentationWrapper>() {
	    public int compare(ThesisPresentationWrapper t1, ThesisPresentationWrapper t2) {
		return Thesis.COMPARATOR_BY_STUDENT.compare(t1.getThesis(), t2.getThesis());
	    }
	};

	protected final Thesis thesis;
	protected final ThesisPresentationState thesisPresentationState;

	public ThesisPresentationWrapper(final Thesis thesis) {
	    this.thesis = thesis;
	    this.thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);
	}

	public Thesis getThesis() {
	    return thesis;
	}

	public ThesisPresentationState getThesisPresentationState() {
	    return thesisPresentationState;
	}

	public boolean isUnexisting() {
	    return thesisPresentationState.isUnexisting();
	}

	public boolean isDraft() {
	    return thesisPresentationState.isDraft();
	}

	public boolean isSubmitted() {
	    return thesisPresentationState.isSubmitted();
	}

	public boolean isRejected() {
	    return thesisPresentationState.isRejected();
	}

	public boolean isApproved() {
	    return thesisPresentationState.isApproved();
	}

	public boolean isDocumentsSubmitted() {
	    return thesisPresentationState.isDocumentsSubmitted();
	}

	public boolean isDocumentsConfirmed() {
	    return thesisPresentationState.isDocumentsConfirmed();
	}

	public boolean isConfirmed() {
	    return thesisPresentationState.isConfirmed();
	}

	public boolean isEvaluated1st() {
	    return thesisPresentationState.isEvaluated1st();
	}

	public boolean isEvaluated() {
	    return thesisPresentationState.isEvaluated();
	}

	public boolean isUnknown() {
	    return thesisPresentationState.isUnknown();
	}

    }

    public static class ThesisPresentationWrapperSet extends TreeSet<ThesisPresentationWrapper> {
	public ThesisPresentationWrapperSet(final Collection<Thesis> theses) {
	    super(ThesisPresentationWrapper.COMPARATOR_BY_STUDENT);
	    for (final Thesis thesis : theses) {
		add(new ThesisPresentationWrapper(thesis));
	    }
	}

	public ThesisPresentationWrapperSet(final Person person, final ThesisParticipationType thesisParticipationType) {
	    this(Thesis.getThesesByParticipationType(person, thesisParticipationType));
	}
    }

    public ActionForward showThesisList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	final Person person = AccessControl.getPerson();
	if (person != null) {
	    final ThesisPresentationWrapperSet orientedTheses = new ThesisPresentationWrapperSet(person,
		    ThesisParticipationType.ORIENTATOR);
	    request.setAttribute("orientedTheses", orientedTheses);
	    final ThesisPresentationWrapperSet coorientedTheses = new ThesisPresentationWrapperSet(person,
		    ThesisParticipationType.COORIENTATOR);
	    request.setAttribute("coorientedTheses", coorientedTheses);
	}

	return mapping.findForward("showThesisList");
    }

    public ActionForward viewThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {
	final String thesisIdString = request.getParameter("thesisID");
	final Integer thesisId = thesisIdString == null ? null : Integer.valueOf(thesisIdString);

	final Thesis thesis = thesisId == null ? null : rootDomainObject.readThesisByOID(thesisId);
	request.setAttribute("thesis", thesis);

	final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);
	request.setAttribute("thesisPresentationState", thesisPresentationState);

	return mapping.findForward("viewThesis");
    }

    public ActionForward showConfirmationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {
	request.setAttribute("showConfirmationPage", Boolean.TRUE);
	return viewThesis(mapping, form, request, response);
    }

    public ActionForward confirmDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {
	final String thesisIdString = request.getParameter("thesisID");
	final Integer thesisId = thesisIdString == null ? null : Integer.valueOf(thesisIdString);

	final Thesis thesis = thesisId == null ? null : rootDomainObject.readThesisByOID(thesisId);
	executeService("ConfirmThesisDocumentSubmission", new Object[] { thesis });

	request.setAttribute("documentsConfirmed", Boolean.TRUE);
	return viewThesis(mapping, form, request, response);
    }

    protected Teacher getLoggedTeacher() {
	final Person person = AccessControl.getPerson();
	if (person != null) {
	    final Teacher teacher = person.getTeacher();
	    return teacher == null ? null : teacher;
	}
	return null;
    }

}
