package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/editCandidacyInformation", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "editCandidacyInformation", path = "/academicAdminOffice/student/registration/editCandidacyInformation.jsp"),
	@Forward(name = "visualizeStudent", path = "/student.do?method=visualizeStudent") })
public class EditCandidacyInformationDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PersonalInformationBean currentPersonalInformationBean = getPersonalInformationBean(request);
	if (currentPersonalInformationBean == null) {
	    return mapping.findForward("visualizeStudent");
	}
	request.setAttribute("personalInformationBean", currentPersonalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    private PersonalInformationBean getPersonalInformationBean(HttpServletRequest request) {
	ChooseRegistrationOrPhd chooseRegistrationOrPhd = getRenderedObject("choosePhdOrRegistration");
	if (chooseRegistrationOrPhd.getPhdRegistrationWrapper() == null) {
	    request.setAttribute("studentID", chooseRegistrationOrPhd.getStudent().getIdInternal());
	    return null;
	}
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (chooseRegistrationOrPhd.getPhdRegistrationWrapper().isRegistration()) {
	    return chooseRegistrationOrPhd.getPhdRegistrationWrapper().getRegistration().getPersonalInformationBean(
		    currentExecutionYear);
	} else {
	    return chooseRegistrationOrPhd.getPhdRegistrationWrapper().getPhdIndividualProgramProcess()
		    .getPersonalInformationBean(currentExecutionYear);
	}
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward prepareEditInstitutionPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	personalInformationBean.resetDegree();
	RenderUtils.invalidateViewState();
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
	personalInformationBean.resetInstitutionAndDegree();
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward changePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");

	final Set<String> messages = personalInformationBean.validateForAcademicService();
	if (!messages.isEmpty()) {
	    for (final String each : messages) {
		addActionMessage(request, each);
	    }
	    request.setAttribute("personalInformationBean", personalInformationBean);
	    return mapping.findForward("editCandidacyInformation");
	}

	try {
	    personalInformationBean.updatePersonalInformation(false);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("personalInformationBean", personalInformationBean);
	    return mapping.findForward("editCandidacyInformation");
	}

	request.setAttribute("studentID", personalInformationBean.getStudent().getIdInternal());
	return mapping.findForward("visualizeStudent");
    }

    public static class PhdRegistrationWrapper implements Serializable {
	private DomainObject phdOrRegistration;

	public PhdRegistrationWrapper(Registration registration) {
	    setPhdOrRegistration(registration);
	}

	public PhdRegistrationWrapper(PhdIndividualProgramProcess phdIndividualProgramProcess) {
	    setPhdOrRegistration(phdIndividualProgramProcess);
	}

	public String getDisplayName() {
	    if (isRegistration()) {
		return getRegistration().getDegreeCurricularPlanName();
	    } else {
		Locale locale = Language.getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);
		StringBuilder stringBuilder = new StringBuilder(bundle.getString("label.phd")).append(" ");
		stringBuilder.append(getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(
			Language.valueOf(locale.getLanguage())));
		return stringBuilder.toString();
	    }
	}

	public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	    return (PhdIndividualProgramProcess) getPhdOrRegistration();
	}

	public Registration getRegistration() {
	    return (Registration) getPhdOrRegistration();
	}

	public boolean isRegistration() {
	    return getPhdOrRegistration() != null && getPhdOrRegistration() instanceof Registration;
	}

	public void setPhdOrRegistration(DomainObject phdOrRegistration) {
	    this.phdOrRegistration = phdOrRegistration;
	}

	public DomainObject getPhdOrRegistration() {
	    return phdOrRegistration;
	}

	@Override
	public int hashCode() {
	    if (!isRegistration()) {
		return getPhdIndividualProgramProcess().hasRegistration() ? getPhdIndividualProgramProcess().getRegistration()
			.hashCode() : getPhdIndividualProgramProcess().hashCode();
	    } else {
		return getRegistration().hashCode();
	    }
	}

	@Override
	public boolean equals(Object object) {
	    if (object != null) {
		PhdRegistrationWrapper phdRegistrationWrapper = (PhdRegistrationWrapper) object;
		return phdRegistrationWrapper.hashCode() == this.hashCode();
	    } else {
		return false;
	    }
	}
    }

    public static class ChooseRegistrationOrPhd implements Serializable {
	private PhdRegistrationWrapper phdRegistrationWrapper;
	private Student student;

	public ChooseRegistrationOrPhd(Student student) {
	    setStudent(student);
	}

	public PhdRegistrationWrapper getPhdRegistrationWrapper() {
	    return phdRegistrationWrapper;
	}

	public void setPhdRegistrationWrapper(PhdRegistrationWrapper phdRegistrationWrapper) {
	    this.phdRegistrationWrapper = phdRegistrationWrapper;
	}

	public void setStudent(Student student) {
	    this.student = student;
	}

	public Student getStudent() {
	    return student;
	}
    }
}
