package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendEmailToDelegates extends SimpleMailSenderAction {

    public Student getDelegate(HttpServletRequest request) {
	final Person person = getLoggedPerson(request);
	return person.getStudent();
    }

    @Override
    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
	List<IGroup> groups = super.getPossibleReceivers(request);

	final Person person = getLoggedPerson(request);

	PersonFunction delegateFunction = null;
	if (person.hasStudent()) {
	    final Student delegate = person.getStudent();
	    final Degree degree = delegate.getLastActiveRegistration().getDegree();
	    delegateFunction = degree.getMostSignificantDelegateFunctionForStudent(delegate, null);

	    /* All other delegates from delegate degree */
	    groups.add(new DelegatesGroup(degree));

	    /* All delegates with same delegate function from other degrees */
	    if (delegateFunction != null
		    && !delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
		for (PersonFunction function : delegate.getAllActiveDelegateFunctions()) {
		    groups.add(new DelegatesGroup(function.getFunction().getFunctionType()));
		}
	    }

	    /* A student can have a GGAE delegate role too */
	    if (person.getActiveGGAEDelegatePersonFunction() != null) {
		groups.add(new DelegatesGroup(person.getActiveGGAEDelegatePersonFunction().getFunction().getFunctionType()));
	    }
	} else {
	    delegateFunction = person.getActiveGGAEDelegatePersonFunction();
	    groups.add(new DelegatesGroup(delegateFunction.getFunction().getFunctionType()));
	}

	return groups;
    }

    @Override
    protected MailBean createMailBean(HttpServletRequest request) {
	MailBean bean = new MailBean();

	Person person = getLoggedPerson(request);
	bean.setFromName(person.getName());
	bean.setFromAddress(person.getEmail());

	bean.setReceiversOptions(getPossibleReceivers(request));

	return bean;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

	MailBean bean = createMailBean(request);
	request.setAttribute("mailBean", bean);

	return mapping.findForward("compose-mail");
    }

    @Override
    public ActionForward send(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

	return super.send(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward sendInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MailBean bean = createMailBean(request);
	request.setAttribute("mailBean", bean);
	return mapping.findForward("compose-mail");
    }
}
