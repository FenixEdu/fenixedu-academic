/*
 * Created on 01/Abr/2005
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;

/**
 * @author João Fialho & Rita Ferreira
 * 
 * 
 */
public class IndexStudentAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        final User userView = Authenticate.getUser();
        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                final Student student = person.getStudent();
                if (student != null) {
                    if (student.hasAnyRegistrations()) {
                        final Registration registration = student.getRegistrationsSet().iterator().next();
                        final InfoStudent infoStudent = new InfoStudent(registration);
                        request.setAttribute("infoStudent", infoStudent);
                        return mapping.findForward("Success");
                    }
                }
            }
        }

        throw new InvalidSessionActionException();
    }

}