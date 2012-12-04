package net.sourceforge.fenixedu.presentationTier.Action.student.exportInformation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/managePasswords", module = "student")
@Forwards({ @Forward(name = "manage.passwords", path = "/student/managePasswords.jsp", tileProperties = @Tile(  title = "private.student.informationexport.passwordmanagement")) })
public class ManagePasswordsAction extends FenixDispatchAction {

    public ActionForward managePasswords(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	return mapping.findForward("manage.passwords");
    }

    protected Student getStudent(final HttpServletRequest httpServletRequest) {
	final IUserView userView = getUserView(httpServletRequest);
	final Person person = userView.getPerson();
	return person.getStudent();
    }

    @Service
    public ActionForward generatePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	final Student student = getStudent(request);
	student.generateExportInformationPassword();
	return managePasswords(mapping, form, request, response);
    }

    @Service
    public ActionForward deletePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	final Student student = getStudent(request);
	student.deleteExportInformationPassword();
	return managePasswords(mapping, form, request, response);
    }

}