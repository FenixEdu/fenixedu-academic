package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.File;
import java.io.Serializable;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsAvailable;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsUnavailable;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.thesis.ThesisFileBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(path = "/manageSecondCycleThesis", module = "scientificCouncil")
@Forwards( {
    @Forward(name = "firstPage", path = "/scientificCouncil/thesis/firstPage.jsp"),
    @Forward(name = "showPersonThesisDetails", path = "/scientificCouncil/thesis/showPersonThesisDetails.jsp"),
    @Forward(name = "showThesisDetails", path = "/scientificCouncil/thesis/showThesisDetails.jsp"),
    @Forward(name = "editThesisEvaluationParticipant", path = "/scientificCouncil/thesis/editThesisEvaluationParticipant.jsp"),
    @Forward(name = "editThesisDetails", path = "/scientificCouncil/thesis/editThesisDetails.jsp"),
    @Forward(name = "addJuryMember", path = "/scientificCouncil/thesis/addJuryMember.jsp")
})
public class ManageSecondCycleThesisDA extends FenixDispatchAction {

    public abstract static class JuryMemberBean implements Serializable {
	ThesisParticipationType thesisParticipationType;

	public ThesisParticipationType getThesisParticipationType() {
	    return thesisParticipationType;
	}

	public void setThesisParticipationType(ThesisParticipationType thesisParticipationType) {
	    this.thesisParticipationType = thesisParticipationType;
	}

	public abstract void addMember(final Thesis thesis);
    }

    public static class InternalJuryMemberBean extends JuryMemberBean {
	Person person;
	Unit unit;

	public Person getPerson() {
	    return person;
	}
	public void setPerson(Person person) {
	    this.person = person;
	}
	public Unit getUnit() {
	    return unit;
	}
	public void setUnit(Unit unit) {
	    this.unit = unit;
	}
	@Override
	public void addMember(final Thesis thesis) {
	    ChangeThesisPerson.add(thesis, thesisParticipationType, person);
	}
    }

    public static class ExternalJuryMemberBean extends JuryMemberBean {
	String personName;
	String unitName;

	public String getPersonName() {
	    return personName;
	}
	public void setPersonName(String personName) {
	    this.personName = personName;
	}
	public String getUnitName() {
	    return unitName;
	}
	public void setUnitName(String unitName) {
	    this.unitName = unitName;
	}
	@Override
	public void addMember(final Thesis thesis) {
	    // TODO Auto-generated method stub
	    
	}
    }

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	request.setAttribute("manageSecondCycleThesisSearchBean", new ManageSecondCycleThesisSearchBean());
	return mapping.findForward("firstPage");
    }

    public ActionForward filterSearch(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ManageSecondCycleThesisSearchBean filterSearchForm = getRenderedObject("filterSearchForm");
	request.setAttribute("manageSecondCycleThesisSearchBean", filterSearchForm);
	return mapping.findForward("firstPage");
    }

    public ActionForward searchPerson(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ManageSecondCycleThesisSearchBean searchPersonForm = getRenderedObject("searchPersonForm");
	final SortedSet<Person> people = searchPersonForm.findPersonBySearchString();
	if (people.size() == 1) {
	    final Person person = people.first();
	    return showPersonThesisDetails(mapping, request, person);
	}
	request.setAttribute("people", people);
	request.setAttribute("manageSecondCycleThesisSearchBean", searchPersonForm);
	return mapping.findForward("firstPage");
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Person person = getDomainObject(request, "personOid");
	return showPersonThesisDetails(mapping, request, person);
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final HttpServletRequest request,
	    final Person person) throws Exception {
	request.setAttribute("person", person);
	return mapping.findForward("showPersonThesisDetails");	    
    }

    public ActionForward showThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	return showThesisDetails(mapping, request, thesis);
    }

    private ActionForward showThesisDetails(final ActionMapping mapping, final HttpServletRequest request,
	    final Thesis thesis) throws Exception {
	if (!thesis.areThesisFilesReadable()) {
	    final ThesisFile thesisFile = thesis.getDissertation();
	    final ThesisFileBean thesisDissertationFileBean = new ThesisFileBean();
	    thesisDissertationFileBean.setTitle(thesisFile.getTitle());
	    thesisDissertationFileBean.setSubTitle(thesisFile.getSubTitle());
	    thesisDissertationFileBean.setLanguage(thesisFile.getLanguage());
	    request.setAttribute("thesisDissertationFileBean", thesisDissertationFileBean);

	    final ThesisFileBean thesisExtendendAbstractFileBean = new ThesisFileBean();
	    request.setAttribute("thesisExtendendAbstractFileBean", thesisExtendendAbstractFileBean);
	}
	request.setAttribute("thesis", thesis);
	return mapping.findForward("showThesisDetails");
    }

    public ActionForward editThesisEvaluationParticipant(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ThesisEvaluationParticipant thesisEvaluationParticipant = getDomainObject(request, "thesisEvaluationParticipantOid");
	request.setAttribute("thesisEvaluationParticipant", thesisEvaluationParticipant);
	return mapping.findForward("editThesisEvaluationParticipant");
    }

    public ActionForward removeThesisEvaluationParticipant(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ThesisEvaluationParticipant thesisEvaluationParticipant = getDomainObject(request, "thesisEvaluationParticipantOid");
	final Thesis thesis = thesisEvaluationParticipant.getThesis();

	ChangeThesisPerson.remove(thesisEvaluationParticipant);

	return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward editThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	request.setAttribute("thesis", thesis);
	return mapping.findForward("editThesisDetails");
    }

    public ActionForward changeThesisFilesVisibility(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	thesis.swapFilesVisibility();
	return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward prepareAddJuryMember(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	request.setAttribute("thesis", thesis);
	JuryMemberBean juryMemberBean = getRenderedObject();
	if (juryMemberBean == null) {
	    juryMemberBean = request.getParameter("external") == null ? new InternalJuryMemberBean() : new ExternalJuryMemberBean();
	}
	request.setAttribute("juryMemberBean", juryMemberBean);
	return mapping.findForward("addJuryMember");
    }

    public ActionForward addJuryMember(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	final JuryMemberBean juryMemberBean = getRenderedObject();
	juryMemberBean.addMember(thesis);
	return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward makeDocumentUnavailable(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	MakeThesisDocumentsUnavailable.run(thesis);
	return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward makeDocumentAvailable(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	MakeThesisDocumentsAvailable.run(thesis);
	return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward substituteDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return substituteDocumant(mapping, request, "CreateThesisDissertationFile");
    }

    public ActionForward substituteExtendedAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return substituteDocumant(mapping, request, "CreateThesisAbstractFile");
    }

    public ActionForward substituteDocumant(final ActionMapping mapping, final HttpServletRequest request,
	    final String service) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	ThesisFileBean bean = getRenderedObject();
	RenderUtils.invalidateViewState();

	if (bean != null && bean.getFile() != null) {
	    File temporaryFile = null;

	    try {
		temporaryFile = FileUtils.copyToTemporaryFile(bean.getFile());
		executeService(service, new Object[] { thesis, temporaryFile,
			bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage() });
	    } finally {
		if (temporaryFile != null) {
		    temporaryFile.delete();
		}
	    }
	}

	return showThesisDetails(mapping, request, thesis);
    }

}
