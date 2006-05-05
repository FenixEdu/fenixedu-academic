/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 5, 2006, 9:10:59 AM
 * 
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.WriteExecutionCourseForum.WriteExecutionCourseForumParameters;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Dummy extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {

	IUserView userView = this.getUserView(request);
	WriteExecutionCourseForumParameters parameters = new WriteExecutionCourseForumParameters();
	parameters.description="Descrição dummy sempre baixo ";
	parameters.name="Forum IRS";
	parameters.owner=userView.getPerson();
	Group group = new PersonGroup(userView.getPerson());
	parameters.readersGroup=group;
	parameters.writersGroup=group;
	try {
	    ServiceManagerServiceFactory.executeService(userView,
	    	"WriteExecutionCourseForum", new Object[]{parameters});
	} catch (Exception e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("Success");
    }
}
