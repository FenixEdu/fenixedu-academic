package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.TeachersManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/teacherAuthorization", module = "scientificCouncil")
@Forwards({ @Forward(name = "createTeacherAuthorization", path = "/scientificCouncil/createTeacherAuthorization.jsp"),
	@Forward(name = "listTeacherAuthorization", path = "/scientificCouncil/listTeacherAuthorization.jsp") })
public class TeacherAuthorizationManagement extends FenixDispatchAction {
    public static class TeacherAuthorizationManagementBean implements Serializable {
	private String istUsername;
	private ProfessionalCategory professionalCategory;
	private ExecutionSemester executionSemester;
	private Integer lessonHours;
	private Boolean canPark;
	private Boolean canHaveCard;

	public TeacherAuthorizationManagementBean() {
	}

	public void setIstUsername(String istUsername) {
	    this.istUsername = istUsername;
	}

	public String getIstUsername() {
	    return istUsername;
	}

	public void setProfessionalCategory(ProfessionalCategory professionalCategory) {
	    this.professionalCategory = professionalCategory;
	}

	public ProfessionalCategory getProfessionalCategory() {
	    return professionalCategory;
	}

	public void setExecutionSemester(ExecutionSemester executionSemester) {
	    this.executionSemester = executionSemester;
	}

	public ExecutionSemester getExecutionSemester() {
	    return executionSemester;
	}

	@Service
	private ExternalTeacherAuthorization create() throws FenixActionException {
	    Person person = User.readUserByUserUId(getIstUsername()).getPerson();
	    
	    for (TeacherAuthorization teacherAuthorization : RootDomainObject.getInstance().getTeacherAuthorization()) {
		if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
		    ExternalTeacherAuthorization auth = (ExternalTeacherAuthorization) teacherAuthorization;
		    if (auth.getTeacher().getPerson() == person && auth.getExecutionSemester() == getExecutionSemester()
			    && auth.getActive()) {
			throw new FenixActionException("already.created.teacher.authorization");
		    }
		}
	    }

	    ExternalTeacherAuthorization eta = new ExternalTeacherAuthorization();
	    eta.setAuthorizer(AccessControl.getPerson());
	    eta.setExecutionSemester(getExecutionSemester());
	    eta.setProfessionalCategory(getProfessionalCategory());
	    eta.setRootDomainObject(RootDomainObject.getInstance());
	    eta.setActive(true);
	    eta.setCanPark(getCanPark());
	    eta.setCanHaveCard(false);
	    eta.setLessonHours(Integer.valueOf(getLessonHours()));
	    if (person.getTeacher() != null) {
		eta.setTeacher(person.getTeacher());
	    } else {
		Teacher teacher = new Teacher(person);
		eta.setTeacher(teacher);
	    }
	    return eta;
	}

	public void setLessonHours(Integer lessonHours) {
	    this.lessonHours = lessonHours;
	}

	public Integer getLessonHours() {
	    return lessonHours;
	}

	public void setCanPark(Boolean canPark) {
	    this.canPark = canPark;
	}

	public Boolean getCanPark() {
	    return canPark;
	}

	public void setCanHaveCard(Boolean canHaveCard) {
	    this.canHaveCard = canHaveCard;
	}

	public Boolean getCanHaveCard() {
	    return canHaveCard;
	}
    }

    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ArrayList<ExternalTeacherAuthorization> teacher = new ArrayList<ExternalTeacherAuthorization>();

	for (TeacherAuthorization teacherAuthorization : RootDomainObject.getInstance().getTeacherAuthorization()) {
	    if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
		teacher.add((ExternalTeacherAuthorization) teacherAuthorization);
	    }
	}

	request.setAttribute("auths", teacher);
	return mapping.findForward("listTeacherAuthorization");
    }

    public ActionForward pre(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("bean", new TeacherAuthorizationManagementBean());
	return mapping.findForward("createTeacherAuthorization");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TeacherAuthorizationManagementBean tamb = ((TeacherAuthorizationManagementBean) getRenderedObject("bean"));
	
	try{
	    tamb.create();
	}catch (FenixActionException e) {
	    RenderUtils.invalidateViewState();
	    request.setAttribute("bean", tamb);
	    request.setAttribute("error", true);
	    return mapping.findForward("createTeacherAuthorization");
	}
	
	return list(mapping, actionForm, request, response);
    }
    
    public ActionForward revoke(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ExternalTeacherAuthorization auth = rootDomainObject.fromExternalId((String)request.getParameter("oid"));
	auth.revoke();
	return list(mapping, actionForm, request, response);
    }
}
