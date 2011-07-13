package net.sourceforge.fenixedu.presentationTier.Action.phd.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdInactivePredicateContainer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdIndividualProgramProcess", module = "student")
@Forwards( {

@Forward(name = "viewProcess", path = "/phd/student/viewProcess.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/student/viewAlertMessages.jsp"),

@Forward(name = "viewAlertMessageArchive", path = "/phd/student/viewAlertMessageArchive.jsp"),

@Forward(name = "viewAlertMessage", path = "/phd/student/viewAlertMessage.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/student/viewProcessAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessageArchive", path = "/phd/student/viewProcessAlertMessageArchive.jsp"),

@Forward(name = "choosePhdProcess", path = "/phd/student/choosePhdProcess.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdIndividualProgramProcess process = getProcess(request);

	if (process != null) {
	    request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));
	}

	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	final Person person = getLoggedPerson(request);
	PhdIndividualProgramProcess process = super.getProcess(request);
	if ((process == null) && (person.getPhdIndividualProgramProcessesCount() == 1)) {
	    process = person.getPhdIndividualProgramProcesses().get(0);
	}
	return process;
    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = getLoggedPerson(request);

	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null) {
	    request.setAttribute("process", process);
	    return mapping.findForward("viewProcess");
	}

	request.setAttribute("processes", person.getPhdIndividualProgramProcesses());
	return mapping.findForward("choosePhdProcess");
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
	return Collections.emptyList();
    }

    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
	return null;
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
	return Collections.emptyList();
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
	return Collections.emptyList();
    }

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
	return null;
    }

}
