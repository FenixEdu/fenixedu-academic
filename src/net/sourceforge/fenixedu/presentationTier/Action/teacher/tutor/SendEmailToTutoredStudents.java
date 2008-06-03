package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendEmailToTutoredStudents extends SimpleMailSenderAction {
	
	public Teacher getTeacher(HttpServletRequest request) {
    	final Person person = getLoggedPerson(request);
    	
    	return person.getTeacher();
    }
	
	@Override
    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
    	List<IGroup> groups = super.getPossibleReceivers(request);
  
    	StudentsByTutorBean receivers = (StudentsByTutorBean)request.getAttribute("receivers");
        
        List<Person> persons = new ArrayList<Person>();
        
        if(receivers != null) {
	        for(Tutorship tutorship : receivers.getStudentsList()){
	    		persons.add(tutorship.getStudent().getPerson());
	    	}
	    }
        
    	groups.add(new FixedSetGroup(persons));
        
        return groups;
    }  
	
	@Override
	protected MailBean createMailBean(HttpServletRequest request) {
		MailBean bean = new MailBean();
        
        Person person = getLoggedPerson(request);
        bean.setFromName(person.getName());
        bean.setFromAddress(person.getEmail());
     
        bean.setReceiversOptions(getPossibleReceivers(request));
        bean.setReceiversGroupList(getPossibleReceivers(request));
        bean.setReceiversGroup(getPossibleReceivers(request).get(0)); //only one group of persons (teacher tutors)
        
        return bean;
	}
	
	@Override
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RenderUtils.invalidateViewState();
        
		final Teacher teacher = getTeacher(request);
		
		if(!teacher.getActiveTutorships().isEmpty()){
			StudentsByTutorBean bean = new StudentsByTutorBean(teacher);
			
			request.setAttribute("receiversBean", bean);
		}
        
		request.setAttribute("tutor", teacher.getPerson());
        return mapping.findForward("choose-receivers");
	}
	
	public ActionForward prepareCreateMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Teacher teacher = getTeacher(request);
		
		StudentsByTutorBean receivers = null;
		if(RenderUtils.getViewState("receivers") != null) {
			receivers = (StudentsByTutorBean)RenderUtils.getViewState("receivers").getMetaObject().getObject();
		
			if(request.getParameter("selectAll") != null) {
				RenderUtils.invalidateViewState();
				receivers.setStudentsList(teacher.getActiveTutorships());
				request.setAttribute("receiversBean", receivers);
			}
			else if(request.getParameter("reset") != null) {
				RenderUtils.invalidateViewState();
				receivers.setStudentsList(new ArrayList<Tutorship>());
				request.setAttribute("receiversBean", receivers);
			}
			else {
				if(receivers.getStudentsList().isEmpty()) {
					addActionMessage(request, "error.teacher.tutor.sendMail.chooseReceivers.mustSelectOne");
					request.setAttribute("receiversBean", receivers);
				}
				else {
					request.setAttribute("receivers", receivers);
					return createMail(mapping, actionForm, request, response);
				}
			}
		}
		
		request.setAttribute("tutor", teacher.getPerson());
		return mapping.findForward("choose-receivers");
	}
	
	public ActionForward createMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Teacher teacher = getTeacher(request);
		
        MailBean bean = createMailBean(request);
        request.setAttribute("mailBean", bean);

		request.setAttribute("tutor", teacher.getPerson());
        return mapping.findForward("compose-mail");
	}
	
	@Override
	public ActionForward sendInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MailBean bean = createMailBean(request);
		
        request.setAttribute("mailBean", bean);
        request.setAttribute("tutor", getLoggedPerson(request));
        return mapping.findForward("compose-mail");
	}
}
